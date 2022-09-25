import jagwarez.game.asset.AssetReader;
import jagwarez.game.asset.model.Animation;
import jagwarez.game.asset.model.Bone;
import jagwarez.game.asset.model.Channel;
import jagwarez.game.asset.model.Effect;
import jagwarez.game.asset.model.Keyframe;
import jagwarez.game.asset.model.Mesh;
import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.Vertex;
import jagwarez.game.asset.model.reader.WavefrontReader;
import java.io.File;
import java.util.List;

/**
 *
 * @author jacob
 */
public class ModelReaderTest {
    
    public static void main(String[] args) throws Exception {
        
        
        AssetReader<Model> modelReader = new WavefrontReader(new File("games/hello/assets/textures/skybox4/skydome.obj"));
        Model model = modelReader.read();
        
        System.out.println("model="+model.name);
        
        for(String meshId : model.meshes.keySet()) {
            Mesh mesh = model.meshes.get(meshId);
            System.out.println("mesh="+meshId);  
             
            
            for(Mesh.Group group : mesh.groups) {
                
                System.out.println("group-"+group.index+":vertices="+group.vertices.size()); 
                
                for(Vertex vertex : group.vertices) {               
                    //System.out.println("\tposition="+vertex.position);

                    //for(Entry<Bone,Float> weight : vertex.weights.entrySet())
                        //System.out.println("\t\tweight: bone="+weight.getKey().name+", weight="+weight.getValue());
                }

                for(Effect.Parameter param : group.material.effects.keySet()) {
                    Effect effect = group.material.effects.get(param);
                    System.out.println(param+"="+effect.type);
                }
            }
        }
        
        for(String animationId : model.animations.keySet()) {
            //if(true) break;
            Animation animation = model.animations.get(animationId);
            System.out.println("animation="+animation.name);
            for(Channel channel : animation.channels.values()) {
                System.out.println("\tchannel="+channel.name);
                System.out.println("\ttarget="+channel.target.name);
                for(Keyframe keyframe : channel.keyframes) {
                    System.out.println("\t\ttime="+keyframe.time);
                    System.out.println("\t\tposition="+keyframe.position);
                    //System.out.println("\t\trotation="+keyframe.rotation);
                    //System.out.println("\t\ttransform=\n"+keyframe.transform);
                    //System.out.println("");
                }
            }
        }
        
        printJoints(model.bones, "");
        
    }
    
    public static void printJoints(List<Bone> bones, String tabs) {
        for(Bone bone : bones) {
            System.out.println(tabs+"bone="+bone.name+" index="+bone.index+" parent="+(bone.parent != null ? bone.parent.name : "null"));
            printJoints(bone.children, tabs+"\t");
        }
    }
           
}
