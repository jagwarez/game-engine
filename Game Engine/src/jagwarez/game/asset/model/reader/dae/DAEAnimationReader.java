package jagwarez.game.asset.model.reader.dae;

import jagwarez.game.asset.model.Animated;
import jagwarez.game.asset.model.Animation;
import jagwarez.game.asset.model.Channel;
import jagwarez.game.asset.model.Keyframe;
import jagwarez.game.asset.model.Model;
import java.io.File;
import javax.xml.xpath.XPathConstants;
import org.joml.Matrix4f;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author jacob
 */
public class DAEAnimationReader extends DAEFileReader<Animation> {
    
    private final Model model;
    
    public DAEAnimationReader(Model model) throws Exception {
        this.model = model;
    }
    
    @Override
    public Animation read(File file) throws Exception {
        
        parse(file);
        
        Animation animation = new Animation(name, model);
        
        Element animationElement = (Element) xpath.evaluate("//library_animations/*[1]", document, XPathConstants.NODE);
        readAnimation(model, animationElement, animation);
        
        model.animations.put(name, animation);
        
        return animation;
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
                                      "JOINT".equals(targetType) ? model.skeleton.anatomy.get(targetNode.getAttribute("sid")) : null;
                    
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
    
}
