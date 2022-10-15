package jagwarez.game.asset.model;

import org.joml.Matrix4f;

/**
 *
 * @author jacob
 */
public abstract class Animated {
    
    public final String name;
    public final Matrix4f local = new Matrix4f();
    public final Matrix4f bind = new Matrix4f();
    public final Matrix4f inverse = new Matrix4f();
    public final Matrix4f transform = new Matrix4f();
    
    public Animated(String name) { 
        this.name = name;
    }
    
    public void pose() {
        transform.identity();
    }
    
    public abstract void animate();
}
