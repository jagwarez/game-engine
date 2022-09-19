package jagwarez.game.asset;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

/**
 *
 * @author jacob
 */
public class Bone extends Animated {
    
    public final int index;
    public final Bone root;
    public final Bone parent;
    public final List<Bone> children;
    public final Matrix4f inverse;

    public Bone(String name, int index, Bone parent) {
        super(name);
        this.index = index;
        this.root = parent != null ? parent.root : this;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.inverse = new Matrix4f();

        if(parent != null)
            parent.children.add(this);
    }
    
    @Override
    public void animate() {
        
        transform.set(local);
        
        if(parent != null)
            parent.transform.mul(transform, transform);
        
        for(Bone child : children)
            child.animate();
        
        transform.mul(inverse);

    }
}
