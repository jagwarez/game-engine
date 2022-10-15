package jagwarez.game.asset.model;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author jacob
 */
public class Keyframe {
    
    public final float time;
    public final Matrix4f transform;
    public final Vector3f position;
    public final Quaternionf rotation;
    public final Vector3f scale;
    
    public Keyframe(float time, Matrix4f transform) {
        this.time = time;
        this.transform = transform;
        this.position = transform.getTranslation(new Vector3f());
        this.rotation = transform.getNormalizedRotation(new Quaternionf());
        this.scale = transform.getScale(new Vector3f());
    }
}
