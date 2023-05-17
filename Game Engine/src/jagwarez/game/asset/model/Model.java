package jagwarez.game.asset.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jacob
 */
public class Model {
    
    public final String name;
    public final Map<String,Mesh> meshes;
    public final Map<String,Animation> animations;
    public final Skeleton skeleton;
    
    public Model(String name) {
        this.name = name;
        this.meshes = new HashMap<>();
        this.animations = new HashMap<>();
        this.skeleton = new Skeleton();
    }
    
    public boolean animated() {
        return !animations.isEmpty();
    }
    
    public boolean skeletal() {
        return !skeleton.bones.isEmpty();
    }
    
    public void animate() {
        skeleton.animate();
    }
    
    public void pose() {
        skeleton.pose();
    }
    
}
