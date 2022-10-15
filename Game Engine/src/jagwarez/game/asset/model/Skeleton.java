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
    public final Map<String,Bone> anatomy;
    
    Bone root = null;
    
    public Skeleton() {
        bones = new ArrayList<>();
        anatomy = new HashMap<>();
    }
    
    public void pose() {
        for(Bone bone : bones)
            bone.pose();
    }
    
    public void add(Bone bone) {
        
        if(root == null)
            root = bone.root;
        
        bones.add(bone);
        anatomy.put(bone.name, bone);
        
    }
        
}
