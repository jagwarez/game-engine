package jagwarez.game.asset.model.reader.dae;

import jagwarez.game.asset.model.Bone;
import jagwarez.game.asset.model.Color;
import jagwarez.game.asset.model.Effect;
import jagwarez.game.asset.model.Mesh;
import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.Texture;
import jagwarez.game.asset.model.Vertex;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author jacob
 */
public class DAEModelReader extends DAEFileReader<Model> {
    
    private Model model;
    
    @Override
    public Model read(File modelFile) throws Exception {
        
        parse(modelFile);
        
        model = new Model(name);
        
        Element element = (Element) xpath.evaluate("//scene/instance_visual_scene", document, XPathConstants.NODE);
        String sceneId = element.getAttribute("url").substring(1);
        element = getElementById("//library_visual_scenes/visual_scene", sceneId);

        NodeList childNodes = (NodeList) xpath.evaluate("child::node", element, XPathConstants.NODESET);
        for(int nodeIndex = 0; nodeIndex < childNodes.getLength(); nodeIndex++)
            readNodes((Element) childNodes.item(nodeIndex));
        
        File animDir = new File(modelFile.getParentFile(), "animations");
        if(animDir.exists() && animDir.isDirectory()) {
            
            File[] animFiles = animDir.listFiles((File f, String fileName) -> fileName.endsWith(FILE_EXT));

            if(animFiles != null) {

                DAEAnimationReader animReader = new DAEAnimationReader(model);

                for(File animFile : animFiles)           
                    animReader.read(animFile);
            }
        }
        
        return model;
    }
    
    private void readNodes(Element nodeElement) throws Exception {
        
        NodeList childNodes = (NodeList) xpath.evaluate("child::*", nodeElement, XPathConstants.NODESET);
        Matrix4f localMatrix = new Matrix4f();
        
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
                    readMesh(meshId, null);
                    
                    break;
                case "instance_controller":
                    
                    String skinId = childElement.getAttribute("url").substring(1);
                    readSkin(skinId);
                    
                    break;
                case "node":
                    
                    String nodeType = childElement.getAttribute("type");
                    if("NODE".equals(nodeType))
                        readNodes(childElement);
                    else if("JOINT".equals(nodeType)) { 
                        readJoint(childElement, null);
                    }
                    
                    break;
            }
        }
    }
    
    private void readJoint(Element jointElement, Bone parent) throws Exception {
        
        String boneName = jointElement.getAttribute("sid");
        
        Bone bone = new Bone(boneName, model.skeleton.bones.size(), parent);
        model.skeleton.add(bone);
        
        NodeList childNodes = (NodeList) xpath.evaluate("child::*", jointElement, XPathConstants.NODESET);
        for(int childIndex = 0; childIndex < childNodes.getLength(); childIndex++) {

            Element childElement = (Element) childNodes.item(childIndex);

            switch(childElement.getTagName()) {
                case "matrix":
                    
                    bone.local.set(readMatrix4f(childElement));
                    
                    if(parent != null) {
                        parent.bind.mul(bone.local, bone.bind);                
                    } else {
                        bone.bind.set(bone.local);                
                    }
                    
                    bone.bind.invert(bone.inverse);
                    
                    break;
                case "node":
                    
                    if("JOINT".equals(childElement.getAttribute("type")))
                        readJoint(childElement, bone);
                    
                    break;
            }
        }
    }
    
    private void readSkin(String skinId) throws Exception {
        
        Element controllerElement = getElementById("//library_controllers/controller", skinId);
        Element skinElement = (Element) xpath.evaluate("child::skin", controllerElement, XPathConstants.NODE);
        //Element bindElement = (Element) xpath.evaluate("child::bind_shape_matrix", skinElement, XPathConstants.NODE);
        //Matrix4f bindMatrix = readMatrix4f(bindElement);
        
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
                    
                    // we're calculating the inverses
                    /*
                    Element matrixElement = (Element) xpath.evaluate("child::float_array", sourceElement, XPathConstants.NODE);
                    Matrix4f[] inverses = readMatrix4fArray(matrixElement);
                    
                    for(int matrixIndex = 0; matrixIndex < inverses.length; matrixIndex++) {
                        String boneId = bones[matrixIndex];
                        Bone bone = model.skeleton.map.get(boneId);
                        
                        if(bone != null) {
                            //bone.inverse.set(inverses[matrixIndex]);
                        }
                    }
                    */
                    
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
                
                Bone bone = model.skeleton.map.get(bones[boneIndex]);
                boneWeights.put(bone, weights[weightIndex]);
                
                dataIndex += 2;
            }
        }
         
        readMesh(skinElement.getAttribute("source").substring(1), skin);
    }
    
    private void readMesh(String meshId, List<Map<Bone,Float>> skin) throws Exception {
        
        Mesh mesh = new Mesh(meshId);
        model.meshes.put(mesh.name, mesh);

        Element meshElement = (Element) getElementById("//library_geometries/geometry", meshId);
        HashMap<String,List> sources = new HashMap<>();
        
        for(String trianglesNodeName : new String[]{"triangles", "polylist"}) {
            
            NodeList trianglesNodes = meshElement.getElementsByTagName(trianglesNodeName);
            
            for(int trianglesIndex = 0; trianglesIndex < trianglesNodes.getLength(); trianglesIndex++) {
                
                Mesh.Group group = new Mesh.Group(0);
                mesh.groups.add(group);
                
                int positionOffset = -1, normalOffset = -1, texcoordOffset = -1;
                Element trianglesElement = (Element) trianglesNodes.item(trianglesIndex);
                
                String materialId = trianglesElement.getAttribute("material");
                readMaterial(group, materialId);
                
                List<Vector4f> positions = null;
                List<Vector4f> normals = null;
                List<Vector2f> coords = null;
                
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
                                break;
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
                            
                            if(texcoordOffset != -1)
                                inputCount--;
                            
                            texcoordOffset = inputOffset;
                            
                            if(sources.containsKey(sourceId)) {
                                coords = sources.get(sourceId);
                                continue;
                            }
                            
                            coords = new ArrayList<>();
                            sources.put(sourceId, coords);
                            
                            String coordData = (String) xpath.evaluate("child::float_array[1]", sourceElement, XPathConstants.STRING);
                            String[] coordArray = coordData.split(" ");
                                                     
                            for(int i = 0; i < coordArray.length;) {
                                float s = Float.parseFloat(coordArray[i++]);
                                float t = 1f - Float.parseFloat(coordArray[i++]);
                                
                                coords.add(new Vector2f(s, t));                           
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
                    vertex.coordinate.set(coords.get(texIndex));
                    
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

                if(param == null)
                    continue;
                
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
                        File imageFile = new File(file.getParentFile(), URLDecoder.decode(imageName, "UTF-8"));
                        
                        if(imageFile.exists())
                            group.material.effects.put(param, new Texture(imageFile));
                        
                        break;
                }
            }
        }
    }
    
}
