package jagwarez.game.asset.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jacob
 */
public class Skeleton {
    
    public final List<Bone> bones;
    public final Map<String,Bone> map;
    
    Bone root = null;
    
    public Skeleton() {
        bones = new ArrayList<>();
        map = new HashMap<>();
    }
    
    public void animate() {
        root.animate();
    }
    
    public void pose() {
        for(Bone bone : bones)
            bone.pose();
    }
    
    public void add(Bone bone) {
        
        if(root == null)
            root = bone.root;
        
        bones.add(bone);
        map.put(bone.name, bone);
        
    }
        
}
