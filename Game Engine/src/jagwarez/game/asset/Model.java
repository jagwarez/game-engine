package jagwarez.game.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jacob
 */
public class Model {
    
    public final String name;
    public final Map<String,Mesh> meshes;
    public final List<Bone> bones;
    public final Map<String,Animation> animations;
   
    public Model(String name) {
        this.name = name;
        this.meshes = new HashMap<>();
        this.bones = new ArrayList<>();
        this.animations = new HashMap<>();
    }
    
    public void animate(Animation animation, float time) {
       animation.play(time);
       if(!bones.isEmpty())
            bones.get(0).animate();
    }
    
    public boolean animated() {
        return !animations.isEmpty();
    }
    
    public boolean skeletal() {
        return !bones.isEmpty();
    }
}
