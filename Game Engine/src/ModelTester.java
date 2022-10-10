
import jagwarez.game.asset.model.Animation;
import jagwarez.game.asset.model.Bone;
import jagwarez.game.asset.model.Channel;
import jagwarez.game.asset.model.Mesh;
import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.reader.ColladaReader;
import java.io.File;
import java.util.List;

/**
 *
 * @author jacob
 */
public class ModelTester {
    public static void main(String[] args) throws Exception {
         Model model = new ColladaReader(new File("games/hello/assets/models/mawjlaygo/mawlaygo.dae")).read();
         
         for(Mesh mesh : model.meshes.values()) {
             System.out.println("mesh="+mesh.name);
             
             for(Mesh.Group group : mesh.groups) {
                 System.out.println("vertices="+group.vertices.size());
             }
         }
         
         for(Animation animation : model.animations.values()) {
             System.out.println("animation="+animation.name);
             
             int i = 0;
             for(Channel channel : animation.channels.values()) {
                 System.out.println("channel-"+(i++)+"="+channel.name);
             }
         }
         
         printBones(model.bones, "");
    }
    
    public static void printBones(List<Bone> bones, String tabs) {
        for(Bone bone : bones) {
            System.out.println(tabs+"bone="+bone.name+" index="+bone.index);
            printBones(bone.children, tabs+"\t");
        }
    }
}
