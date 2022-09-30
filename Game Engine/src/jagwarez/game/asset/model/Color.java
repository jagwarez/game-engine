package jagwarez.game.asset.model;

import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 *
 * @author jacob
 */
public class Color extends Effect {
    
    public float r, g, b, a;
    
    public Color() {
        this(0f,0f,0f,1f);
    }
    
    public Color(float r, float g, float b, float a) {
        super(Type.COLOR);
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public Vector3f rbg() {
        return new Vector3f(r,g,b);
    }
    
    public Vector4f rgba() {
        return new Vector4f(r,g,b,a);
    }
}
