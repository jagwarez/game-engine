package jagwarez.game.engine;

import jagwarez.game.asset.model.Color;
import org.joml.Vector3f;

/**
 *
 * @author jacob
 */
public class Light extends Identity {
    
    public final Vector3f position;
    public final Vector3f attenuation;
    public final Color color;
    public float intensity = 1f;
    public float radius = 10f;
    
    public Light() {
        position = new Vector3f();
        attenuation = new Vector3f(1f, .01f, .002f);
        color = new Color();
    }
    
    public Light color(float r, float g, float b) {
        color.rgb(r, g, b);
        return this;
    }
    
    public Light position(float x, float y, float z) {
        position.set(x, y, z);
        return this;
    }
    
    public Light radius(float r) {
        radius = r;
        return this;
    }
    
}
