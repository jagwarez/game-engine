/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

/**
 *
 * @author jacob
 */
public class Bone extends Animated {
    
    public final int index;
    public final Bone parent;
    public final List<Bone> children;
    public final Matrix4f inverse;

    public Bone(String name, int index, Bone parent) {
        super(name);
        this.index = index;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.inverse = new Matrix4f();
    }
    
    @Override
    public void animate() {
        //System.out.println("Animating bone "+name+" parent="+(parent != null ? parent.name : "null"));
        if(parent != null)
            parent.transform.mul(transform, transform);
        for(Bone child : children)
            child.animate();
        transform.mul(inverse);
    }
}
