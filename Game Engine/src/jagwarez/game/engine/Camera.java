package jagwarez.game.engine;

import org.joml.Vector3f;

/**
 *
 * @author jacob
 */
public class Camera extends Actor {
    
    public Actor target;
    public float zoom = 1f;
    
    public Camera() {
        super("camera");
        target = this;
    }
    
    @Override
    public Entity update() {
        
        if(target != null && target.id != this.id) {
            position.set(target.update().position).add(new Vector3f(0,10f,-30f));
        } else {
            move();
        }
        
        identity();
        
        scale(zoom);
        rotateXYZ((float)Math.toRadians(-rotation.x), (float)Math.toRadians(-rotation.y+180), (float)Math.toRadians(-rotation.z));
        translate(-position.x , -position.y, -position.z);
        
        return this;
    }
    
}
