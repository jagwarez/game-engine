/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author jacob
 */
public class Channel {
    
    public final String name;
    public final Animated target;
    public final List<Keyframe> keyframes;
    private float duration = 0f;
    
    public Channel(String name, Animated target) {
        this.name = name;
        this.target = target;
        this.keyframes = new ArrayList<>();
    }
    
    public void animate(float time) {
        
        if(target != null) {
            time %= keyframes.get(keyframes.size()-1).time;
            Keyframe lastframe = keyframes.get(0);
            for(Keyframe keyframe : keyframes) {              
                if(keyframe.time > time) {
                    //System.out.println("Animation "+name+": time="+time+" key="+keyframe.time+" transform=\n"+keyframe.transform);

                    float progression = (time-lastframe.time)/(keyframe.time - lastframe.time);
                    Vector3f position = lastframe.position.lerp(keyframe.position, progression, new Vector3f());
                    Quaternionf rotation = lastframe.rotation.nlerp(keyframe.rotation, progression, new Quaternionf());
                    
                    Matrix4f transform = target.transform;                  
                    transform.identity(); 
                    transform.translate(position);
                    transform.rotate(rotation);
                    
                    break;
                }
                lastframe = keyframe;
            }
        }
    }
}
