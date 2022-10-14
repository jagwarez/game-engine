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
        
        scale(zoom);
        
        if(target != null && target.id != this.id) {
            
            target.update();
            
            position.x = target.position.x;
            position.y = target.position.y;
            position.z = target.position.z;
            position.add(new Vector3f(0f,10f,-30f));
            
            lookAt(position, target.position.add(0,10,0, new Vector3f()), World.UP);
            
        } else {
            
            move();
            
            rotateXYZ((float)Math.toRadians(-rotation.x), (float)Math.toRadians(-rotation.y+180), (float)Math.toRadians(-rotation.z));
            translate(-position.x , -position.y, -position.z);
        
        }
        
        return this;
    }
    
}
