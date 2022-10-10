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
        weight = 0f;
    }
    
    @Override
    public Entity update() {
        
        identity();
        
        if(target != null && target.id != this.id) {
            lookAt(position, target.position.add(0,10,0, new Vector3f()), World.UP);
            position.set(target.update().position).add(new Vector3f(0f,10f,-30f));
        } else {
            
            move();
            
            rotateXYZ((float)Math.toRadians(-rotation.x), (float)Math.toRadians(-rotation.y+180), (float)Math.toRadians(-rotation.z));
            translate(-position.x , -position.y, -position.z);
        
        }
        
        scale(zoom);
        
        return this;
    }
    
}
