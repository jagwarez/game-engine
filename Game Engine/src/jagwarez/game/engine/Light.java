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
    
    public Light() {
        position = new Vector3f();
        attenuation = new Vector3f();
        color = new Color(1f,0,.1f,1f);
    }
}
