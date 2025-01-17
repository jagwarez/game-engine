package jagwarez.game.asset.model;

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
        
        time *= .001;
        time %= keyframes.get(keyframes.size()-1).time;
        
        Keyframe nextFrame = null;
        Keyframe prevFrame = null;
        float prevTime = 0f;
        
        for(Keyframe keyframe : keyframes) {
            nextFrame = keyframe;

            if(nextFrame.time > time)
                break;

            prevFrame = keyframe;
        }
        
        if(prevFrame == null) {
            prevFrame = keyframes.get(keyframes.size()-1);
        } else
            prevTime = prevFrame.time;

        float blend = (time-prevTime)/(nextFrame.time-prevTime);
        Vector3f position = prevFrame.position.lerp(nextFrame.position, blend, new Vector3f());
        Quaternionf rotation = prevFrame.rotation.slerp(nextFrame.rotation, blend, new Quaternionf());
        Vector3f scale = prevFrame.scale.lerp(nextFrame.scale, blend, new Vector3f());
        
        target.transform.identity();
        target.transform.translate(position);
        target.transform.rotate(rotation);
        target.transform.scale(scale);
        
    }
}
