package jagwarez.game.engine;

import jagwarez.game.asset.model.Color;
import org.joml.Vector3f;

/**
 *
 * @author jacob
 */
public class Light {
    
    public final Vector3f position;
    public final Color color;
    
    public Light() {
        position = new Vector3f();
        color = new Color();
    }
}
