package jagwarez.game.asset.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jacob
 */
public class Animation {

    public final String name;
    public final Model model;
    public final Map<String,Channel> channels;
    private float speed = .001f;
    private long duration = -1;
    
    public Animation(String name, Model model) {
        this.name = name;
        this.model = model;
        this.channels = new HashMap<>();
    }

    public void play(float time) {
        
        for(Bone bone : model.skeleton.bones)
            bone.transform.set(bone.local);
        
        for(Channel channel : channels.values())
            channel.play(time);
        
        model.animate();
    }
}
