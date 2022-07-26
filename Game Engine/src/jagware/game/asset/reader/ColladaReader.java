package jagware.game.asset.reader;

import jagware.game.asset.Animated;
import jagware.game.asset.Animation;
import jagware.game.asset.Channel;
import jagware.game.asset.Color;
import jagware.game.asset.Effect;
import jagware.game.asset.Bone;
import jagware.game.asset.Keyframe;
import jagware.game.asset.Model;
import jagware.game.asset.Mesh;
import jagware.game.asset.Texture;
import jagware.game.asset.Vertex;
import java.io.File;
import java.net.URLDecoder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.*;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author jacob
 */
public class ColladaReader implements AssetReader<Model> {
    
    private final FilesReader files;
    private File colladaFile;
    private Document document;
    private XPath xpath;
    private Map<String,Bone> boneMap;
    
    public ColladaReader(File file) {
        this.files = new FilesReader(file);
    }
    
    @Override
    public Model read() throws Exception {
        
        this.colladaFile = files.read();
        
        if(this.colladaFile == null)
            return null;
        
        boneMap = new HashMap<>();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        this.document = builder.parse(colladaFile);
        this.xpath = XPathFactory.newInstance().newXPath();
        
        Model model = new Model(colladaFile.getName().replace(".dae", ""));
        
        Element element = (Element) xpath.evaluate("//scene/instance_visual_scene", document, XPathConstants.NODE);
        String sceneId = element.getAttribute("url").substring(1);
        element = getElementById("//library_visual_scenes/visual_scene", sceneId);

        NodeList childNodes = (NodeList) xpath.evaluate("child::node", element, XPathConstants.NODESET);
        for(int nodeIndex = 0; nodeIndex < childNodes.getLength(); nodeIndex++)
            readNodes(model, (Element) childNodes.item(nodeIndex));
        
        childNodes = (NodeList) xpath.evaluate("//library_animations/child::animation", document, XPathConstants.NODESET);
        for(int nodeIndex = 0; nodeIndex < childNodes.getLength(); nodeIndex++) {
            readAnimation(model, (Element) childNodes.item(nodeIndex), null);
        }
        
        return model;
    }
    
    private void readNodes(Model model, Element nodeElement) throws Exception {
        
        NodeList childNodes = (NodeList) xpath.evaluate("child::*", nodeElement, XPathConstants.NODESET);
        Matrix4f localMatrix = new Matrix4f().identity();
        
        for(int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {

            Element childElement = (Element) childNodes.item(childIndex);

            switch(childElement.getTagName()) {
                case "translate":
                    float[] position = readFloatArray(childElement);                               
                    localMatrix.translate(position[0], position[1], position[2]);

                    break;                                  
                case "rotate":
                    float[] rotation = readFloatArray(childElement);                                
                    localMatrix.rotate(rotation[3], rotation[0], rotation[1], rotation[2]);

                    break;
                case "scale":
                    float[] scale = readFloatArray(childElement);                                   
                    localMatrix.scale(scale[0], scale[1], scale[2]);

                    break;
                case "matrix":

                    localMatrix.set(readMatrix4f(childElement));

                    break;
                case "instance_geometry":
                    
                    String meshId = childElement.getAttribute("url").substring(1);
                    readMesh(model, meshId, null);
                    
                    break;
                case "instance_controller":
                    
                    String skinId = childElement.getAttribute("url").substring(1);
                    readSkin(model, skinId);
                    
                    break;
                case "node":
                    
                    String nodeType = childElement.getAttribute("type");
                    if("NODE".equals(nodeType))
                        readNodes(model, childElement);
                    else if("JOINT".equals(nodeType))
                        readJoint(model, childElement, null);

                    break;
            }
        }
    }
    
    private void readJoint(Model model, Element jointElement, Bone parent) throws Exception {
        
        if(!"JOINT".equals(jointElement.getAttribute("type")))
            return;
        
        String boneName = jointElement.getAttribute("sid");
        
        Bone bone = new Bone(boneName, model.bones.size(), parent);
        model.bones.add(bone);     
        boneMap.put(boneName, bone);
        
        NodeList childNodes = (NodeList) xpath.evaluate("child::*", jointElement, XPathConstants.NODESET);
        for(int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {

            Element childElement = (Element) childNodes.item(childIndex);

            switch(childElement.getTagName()) {
                case "matrix":
                    
                    bone.local.set(readMatrix4f(childElement));
                    
                    break;
                case "node":
                    
                    readJoint(model, childElement, bone);
                    
                    break;
            }
        }
    }
    
    private void readSkin(Model model, String skinId) throws Exception {
        
        Element controllerElement = getElementById("//library_controllers/controller", skinId);
        Element skinElement = (Element) xpath.evaluate("child::skin", controllerElement, XPathConstants.NODE);
        Element bindElement = (Element) xpath.evaluate("child::bind_shape_matrix", skinElement, XPathConstants.NODE);
        Matrix4f bindMatrix = readMatrix4f(bindElement);
        
        List<Map<Bone,Float>> skin = new ArrayList<>();
        String[] bones = null;
        float[] weights = null;
           
        Element jointsElement = (Element) skinElement.getElementsByTagName("joints").item(0);
        NodeList inputNodes = (NodeList) xpath.evaluate("child::input", jointsElement, XPathConstants.NODESET);
        for(int inputIndex = 0; inputIndex < inputNodes.getLength(); inputIndex++) {
            Element inputElement = (Element) inputNodes.item(inputIndex);    
            String sourceId = inputElement.getAttribute("source").substring(1);
            Element sourceElement = getElementById("child::source", sourceId, skinElement);
            
            switch(inputElement.getAttribute("semantic")) {
                case "JOINT":
                    String boneData = (String) xpath.evaluate("child::Name_array/text()", sourceElement, XPathConstants.STRING);
                    bones = boneData.split(" ");
                    
                    break;
                case "INV_BIND_MATRIX":
                    Element matrixElement = (Element) xpath.evaluate("child::float_array", sourceElement, XPathConstants.NODE);
                    Matrix4f[] inverses = readMatrix4fArray(matrixElement);
                    
                    for(int matrixIndex = 0; matrixIndex < inverses.length; matrixIndex++) {
                        String boneId = bones[matrixIndex];
                        Bone bone = boneMap.get(boneId);
                        
                        if(bone != null)
                            bone.inverse.set(inverses[matrixIndex]);
                    }
                    
                    break;
            }
        }
        
        int boneOffset = -1, weightOffset = -1;
        Element vertexElement = (Element) xpath.evaluate("vertex_weights", skinElement, XPathConstants.NODE);
        inputNodes = vertexElement.getElementsByTagName("input");
        for(int inputIndex = 0; inputIndex < inputNodes.getLength(); inputIndex++) {
            Element inputElement = (Element) inputNodes.item(inputIndex);
   
            switch(inputElement.getAttribute("semantic")) {
                case "JOINT":
                    boneOffset = Integer.parseInt(inputElement.getAttribute("offset"));
                    break;
                case "WEIGHT":
                    String weightsId = inputElement.getAttribute("source").substring(1);
                    Element weightsElement = getElementById("source", weightsId, skinElement);
                    weights = readFloatArray((Element) weightsElement.getElementsByTagName("float_array").item(0));
                    weightOffset = Integer.parseInt(inputElement.getAttribute("offset"));
                    break;
            }
        }
        
        int dataIndex = 0;
        String vcountData = (String) xpath.evaluate("child::vcount/text()", vertexElement, XPathConstants.STRING);
        String vData = (String) xpath.evaluate("child::v/text()", vertexElement, XPathConstants.STRING);
        String[] boneCounts = vcountData.split(" ");
        String[] vertexBones = vData.split(" ");
        
        for (String boneCount1 : boneCounts) {
            HashMap<Bone,Float> boneWeights = new HashMap<>();
            skin.add(boneWeights);
            int boneCount = Integer.parseInt(boneCount1);
            for(int countIndex = 0; countIndex < boneCount; countIndex++) {
                int boneIndex = Integer.parseInt(vertexBones[dataIndex + boneOffset]);
                int weightIndex = Integer.parseInt(vertexBones[dataIndex + weightOffset]);
                
                Bone bone = boneMap.get(bones[boneIndex]);
                boneWeights.put(bone, weights[weightIndex]);
                
                dataIndex += 2;
            }
        }
         
        readMesh(model, skinElement.getAttribute("source").substring(1), skin);
 
    }
    
    private void readMesh(Model model, String meshId, List<Map<Bone,Float>> skin) throws Exception {
        
        Mesh mesh = new Mesh(meshId);
        model.meshes.put(mesh.name, mesh);

        Element meshElement = (Element) getElementById("//library_geometries/geometry", meshId);
        HashMap<String,List> sources = new HashMap<>();
        
        for(String trianglesNodeName : new String[]{"triangles", "polylist"}) {
            
            NodeList trianglesNodes = meshElement.getElementsByTagName(trianglesNodeName);
            
            for(int trianglesIndex = 0; trianglesIndex < trianglesNodes.getLength(); trianglesIndex++) {
                
                Mesh.Group group = new Mesh.Group(mesh.groups.size());
                mesh.groups.add(group);
                
                int positionOffset = -1, normalOffset = -1, texcoordOffset = -1;
                Element trianglesElement = (Element) trianglesNodes.item(trianglesIndex);
                
                String materialId = trianglesElement.getAttribute("material");
                readMaterial(group, materialId);
                
                List<Vector4f> positions = null;
                List<Vector4f> normals = null;
                List<Vector2f> texcoords = null;
                
                NodeList inputNodes = trianglesElement.getElementsByTagName("input");
                int inputCount = inputNodes.getLength();
                for(int inputIndex = 0; inputIndex < inputCount; inputIndex++) {
                    
                    Element inputElement = (Element) inputNodes.item(inputIndex);
                    String sourceId = inputElement.getAttribute("source").substring(1);
                    String inputType = inputElement.getAttribute("semantic");
                    int inputOffset = Integer.parseInt(inputElement.getAttribute("offset"));
                    
                    Element sourceElement = getElementById("mesh/source", sourceId, meshElement);
                    
                    switch (inputType) {
                        case "VERTEX":
                            
                            positionOffset = inputOffset;
                            
                            if(sources.containsKey(sourceId)) {
                                positions = sources.get(sourceId);
                                continue;
                            }
                            
                            positions = new ArrayList<>();
                            sources.put(sourceId, positions);
                            
                            Element verticesElement = getElementById("mesh/vertices[1]", sourceId, meshElement);
                            inputElement = (Element) xpath.evaluate("input[1]", verticesElement, XPathConstants.NODE);
                            sourceId = inputElement.getAttribute("source").substring(1);
                            sourceElement = getElementById("mesh/source", sourceId, meshElement);
                            String vertexData = (String) xpath.evaluate("child::float_array[1]", sourceElement, XPathConstants.STRING);
                            String[] vertexArray = vertexData.split(" ");

                            for(int i = 0; i < vertexArray.length;) {
                                Vector4f position = new Vector4f();
                                
                                position.x = Float.parseFloat(vertexArray[i++]);
                                position.y = Float.parseFloat(vertexArray[i++]);
                                position.z = Float.parseFloat(vertexArray[i++]);
                                position.w = 1.0f;
                                
                                positions.add(position);
                            }
                            
                            break;
                            
                        case "NORMAL":
                            
                            normalOffset = inputOffset;
                            
                            if(sources.containsKey(sourceId)) {
                                normals = sources.get(sourceId);
                                continue;
                            }
                            
                            normals = new ArrayList<>();
                            sources.put(sourceId, normals);
                            
                            String normalData = (String) xpath.evaluate("child::float_array[1]", sourceElement, XPathConstants.STRING);
                            String[] normalArray = normalData.split(" ");
                            
                            for(int i = 0; i < normalArray.length;) {
                                float x = Float.parseFloat(normalArray[i++]);
                                float y = Float.parseFloat(normalArray[i++]);
                                float z = Float.parseFloat(normalArray[i++]);
                                
                                Vector4f normal = new Vector4f(x, y, z, 0.0f);

                                normals.add(normal);                           
                            }
                            
                            break;
                            
                        case "TEXCOORD":
                            
                            texcoordOffset = inputOffset;
                            
                            if(sources.containsKey(sourceId)) {
                                texcoords = sources.get(sourceId);
                                continue;
                            }
                            
                            texcoords = new ArrayList<>();
                            sources.put(sourceId, texcoords);
                            
                            String coordData = (String) xpath.evaluate("child::float_array[1]", sourceElement, XPathConstants.STRING);
                            String[] coordArray = coordData.split(" ");
                                                     
                            for(int i = 0; i < coordArray.length;) {
                                float s = Float.parseFloat(coordArray[i++]);
                                float t = 1f - Float.parseFloat(coordArray[i++]);
                                
                                texcoords.add(new Vector2f(s, t));                           
                            }
                            
                            break;
                    }
                }

                String[] triangles = trianglesElement.getElementsByTagName("p").item(0).getFirstChild().getNodeValue().split(" ");
                for(int i = 0; i < triangles.length; i += inputCount) {
                    int positionIndex = Integer.parseInt(triangles[i + positionOffset]);
                    int normalIndex = Integer.parseInt(triangles[i + normalOffset]);
                    int texIndex = Integer.parseInt(triangles[i + texcoordOffset]);

                    Vertex vertex = new Vertex(group.vertices.size());
                    vertex.position.set(positions.get(positionIndex));
                    vertex.normal.set(normals.get(normalIndex));
                    vertex.texcoord.set(texcoords.get(texIndex));

                    if(skin != null)
                        vertex.weights.putAll(skin.get(positionIndex));

                    group.vertices.add(vertex);
                }
            }
        }
     
    }
    
    private void readMaterial(Mesh.Group group, String materialId) throws Exception {
        Element materialElement = getElementById("//library_materials/material", materialId);
        NodeList effectNodes = materialElement.getElementsByTagName("instance_effect");
        
        for(int effectIndex = 0; effectIndex < effectNodes.getLength(); effectIndex++) {
            Element effectElement = (Element) effectNodes.item(effectIndex);
            String effectId = effectElement.getAttribute("url").substring(1);
            effectElement = getElementById("//library_effects/effect", effectId);
            
            Element techniqueElement = (Element) xpath.evaluate("descendant::technique/child::*[1]", effectElement, XPathConstants.NODE);
            NodeList paramNodes = (NodeList) xpath.evaluate("child::*", techniqueElement, XPathConstants.NODESET);
            for(int paramIndex = 0; paramIndex < paramNodes.getLength(); paramIndex++) {
                Element paramElement = (Element) paramNodes.item(paramIndex);
                String paramId = paramElement.getTagName();
                Effect.Parameter param = Effect.Parameter.fromString(paramId);

                if(param == null) continue;
                
                Element valueElement = (Element) xpath.evaluate("child::*[1]", paramElement, XPathConstants.NODE);
                switch(valueElement.getTagName()) {
                    case "color":
                        float[] values = readFloatArray(valueElement);
                        Color color = new Color();
                        color.r = values[0];
                        color.g = values[1];
                        color.b = values[2];
                        color.a = values[3];
                        
                        group.material.effects.put(param, color);
                        
                        break;
                    case "texture":
                        String samplerId = valueElement.getAttribute("texture");
                        String sourceId = (String) xpath.evaluate("descendant::newparam[@sid='"+samplerId+"']/sampler2D/source/text()", effectElement, XPathConstants.STRING);
                        String imageId = (String) xpath.evaluate("descendant::newparam[@sid='"+sourceId+"']/surface/init_from/text()", effectElement, XPathConstants.STRING);
                        String imageName = (String) xpath.evaluate("//library_images/image[@id='"+imageId+"']/init_from/text()", effectElement, XPathConstants.STRING);
                        
                        File imageFile = new File(colladaFile.getParentFile(), URLDecoder.decode(imageName, "UTF-8"));
                        if(imageFile.exists()) {
                            Texture texture = new Texture(imageFile);
                            group.material.effects.put(param, texture);
                        }
                        
                        break;
                }
            }
        }
    }
    
    private void readAnimation(Model model, Element animationElement, Animation animation) throws Exception {
        
        if(animation == null) {
            String animationName = !animationElement.getAttribute("name").isEmpty() ?
                                        animationElement.getAttribute("name") :
                                        animationElement.getAttribute("id").replaceAll("_.*", "");
            
            if(!model.animations.containsKey(animationName)) {
                animation = new Animation(animationName, model);
                model.animations.put(animationName, animation);
            } else
                animation = model.animations.get(animationName);
        }
           
        NodeList childNodes = (NodeList) xpath.evaluate("child::*[self::animation or self::channel]", animationElement, XPathConstants.NODESET);
        for(int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {
            Element childElement = (Element) childNodes.item(childIndex);
            switch(childElement.getTagName()) {
                case "animation":
                     
                    readAnimation(model, childElement, animation);
                    
                    break;
                case "channel":
                 
                    String[] targetData = childElement.getAttribute("target").split("/");
                    String targetId = targetData[0];
                    //String targetAction = targetData[1];

                    Element targetNode = (Element) getElementById("//library_visual_scenes/visual_scene/descendant::node", targetId);
                    
                    if(targetNode == null)
                        continue;
                    
                    String targetType = targetNode.getAttribute("type");
                    Animated target = "NODE".equals(targetType) ? model.meshes.get(targetId) : 
                                      "JOINT".equals(targetType) ? boneMap.get(targetNode.getAttribute("sid")) : null;
                    
                    if(target == null)
                        continue;
                    
                    Channel channel = new Channel(animationElement.getAttribute("id"), target);
                    animation.channels.put(channel.name, channel);
                    
                    float[] times = null;
                    Matrix4f[] transforms = null;

                    String samplerId = childElement.getAttribute("source").substring(1);
                    NodeList inputNodes = getElementById("sampler", samplerId, animationElement).getElementsByTagName("input");
                    for(int inputIndex = 0; inputIndex < inputNodes.getLength(); inputIndex++) {
                        Element inputElement = (Element) inputNodes.item(inputIndex);
                        String sourceId = inputElement.getAttribute("source").substring(1);
                        Element sourceElement = getElementById("source", sourceId, animationElement);

                        switch(inputElement.getAttribute("semantic")) {
                            case "INPUT":
                                Element timeElement = (Element) xpath.evaluate("child::float_array[1]", sourceElement, XPathConstants.NODE);
                                times = readFloatArray(timeElement);
                                break;
                            case "OUTPUT":
                                Element matrixElement = (Element) xpath.evaluate("child::float_array[1]", sourceElement, XPathConstants.NODE);
                                transforms = readMatrix4fArray(matrixElement);
                                break;
                            case "INTERPOLATION":

                                break;
                        }
                    }
                    
                    if(times != null && transforms != null && times.length == transforms.length) {
                        for(int timeIndex = 0; timeIndex < times.length; timeIndex++) {
                            float time = times[timeIndex];
                            Matrix4f transform = transforms[timeIndex];
                            channel.keyframes.add(new Keyframe(time, transform));
                        }
                    }
                    
                    break;
            }
        }
    }
    
    private Matrix4f readMatrix4f(Element matrixElement) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        buffer.put(readFloatArray(matrixElement));
        buffer.flip();
        
        return new Matrix4f(buffer).transpose();
    }
    
    private Matrix4f[] readMatrix4fArray(Element matrixElement) {
        float[] floats = readFloatArray(matrixElement);
        Matrix4f[] matrixArray = new Matrix4f[floats.length/16];
        
        for(int i = 0; i < matrixArray.length; i++) {
            FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
            for(int j = 0; j < 16; j++) {
                buffer.put(floats[16*i+j]);
            }
            buffer.flip();
            
            matrixArray[i] = new Matrix4f(buffer).transpose();
        }
        
        return matrixArray;
    }
    
    private float[] readFloatArray(Element floatElement) {
        String[] floats = floatElement.getFirstChild().getNodeValue().split(" ");
        float[] floatArray = new float[floats.length];
        
        for(int i = 0; i < floatArray.length; i++)
            floatArray[i] = Float.parseFloat(floats[i]);
        
        return floatArray;
    }
    
    private Element getElementById(String path, String id, Node node) throws Exception {
        return (Element) xpath.evaluate(String.format("%s[@id='%s']", path, id), node, XPathConstants.NODE);
    }
    
    private Element getElementById(String path, String id) throws Exception {
        return getElementById(path, id, document);
    }
}
