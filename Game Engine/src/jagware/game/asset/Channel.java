/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

import java.util.ArrayList;
import java.util.List;
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
    
    public void play(float time) {
        
        time %= keyframes.get(keyframes.size()-1).time;

        Keyframe nextframe = keyframes.get(0);
        Keyframe lastframe = keyframes.get(0);
        
        for(Keyframe keyframe : keyframes) {
            nextframe = keyframe;

            if(keyframe.time > time)
                break;

            lastframe = keyframe;
        }

        float blend = (time-lastframe.time)/(nextframe.time-lastframe.time);
        Vector3f position = lastframe.position.lerp(nextframe.position, blend, new Vector3f());
        Quaternionf rotation = lastframe.rotation.slerp(nextframe.rotation, blend, new Quaternionf());
        
        target.transform.identity();
        target.transform.translate(position);
        target.transform.rotate(rotation);
        
    }
}
