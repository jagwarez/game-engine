/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import jagware.game.asset.Animation;
import jagware.game.asset.Channel;
import jagware.game.asset.Effect;
import jagware.game.asset.Joint;
import jagware.game.asset.Keyframe;
import jagware.game.asset.Mesh;
import jagware.game.asset.Model;
import jagware.game.asset.reader.ColladaReader;
import java.io.File;
import java.util.List;

/**
 *
 * @author jacob
 */
public class Testing {
    
    public static void main(String[] args) throws Exception {
        
        
        ColladaReader modelReader = new ColladaReader(new File("games/hello/assets/models/thinmatrix/model.dae"));
        Model model = modelReader.read();
        
        System.out.println("model="+model.name);
        
        for(String meshId : model.meshes.keySet()) {
            Mesh mesh = model.meshes.get(meshId);
            System.out.println("mesh="+meshId);  
            System.out.println("vertices="+mesh.vertices.size());  
            System.out.println("triangles="+mesh.triangles.size());
            
            for(Effect.Parameter param : mesh.material.effects.keySet()) {
                Effect effect = mesh.material.effects.get(param);
                System.out.println(param+"="+effect.type);
            }
        }
        
        for(String animationId : model.animations.keySet()) {
            Animation animation = model.animations.get(animationId);
            System.out.println("animation="+animation.name);
            for(Channel channel : animation.channels.values()) {
                System.out.println("\tchannel="+channel.name);
                for(Keyframe keyframe : channel.keyframes) {
                    System.out.println("\t\tkeyframe="+keyframe.time);
                    //System.out.println("\t\ttransform=\n"+keyframe.transform);
                }
            }
        }
        
        //printJoints(model.joints, "");
        
    }
    
    public static void printJoints(List<Joint> joints, String tabs) {
        for(Joint joint : joints) {
            System.out.println(tabs+"joint="+joint.name+" index="+joint.index);
            printJoints(joint.children, tabs+"\t");
        }
    }
           
}
