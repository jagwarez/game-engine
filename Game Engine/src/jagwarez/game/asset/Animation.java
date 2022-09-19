package jagwarez.game.asset;

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
    private float duration = 0f;
    
    public Animation(String name, Model model) {
        this.name = name;
        this.model = model;
        this.channels = new HashMap<>();
    }
    
    public void play(float time) {
        for(Channel channel : channels.values())
            channel.play(time);
    }
}
