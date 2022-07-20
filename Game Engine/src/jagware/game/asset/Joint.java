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
public class Joint extends Animated {
    
    public final int index;
    public final Joint parent;
    public final List<Joint> children;
    public final Matrix4f inverse;

    public Joint(String name, int index, Joint parent) {
        super(name);
        this.index = index;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.inverse = new Matrix4f();
    }
    
    @Override
    public void animate() {
        //System.out.println("Animating joint "+name+" parent="+(parent != null ? parent.name : "null"));
        if(parent != null)
            parent.transform.mul(transform, transform);
        for(Joint child : children)
            child.animate();
        transform.mul(inverse);
    }
}
