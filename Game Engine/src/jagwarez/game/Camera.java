package jagwarez.game;

/**
 *
 * @author jacob
 */
public class Camera extends Entity {
    
    public Tether tether = null;
    public float zoom = 1f;
    
    public Camera() {
        super("camera");
    }
    
    public void follow(Entity target) {
        this.tether = new Tether(target);
    }
    
    @Override
    public Entity update() {
        
        identity();
        
        if(tether != null) {
            Entity target = tether.target;
            position.x = target.position.x;
            position.y = target.position.y + .1f;
            position.z = target.position.z - tether.distance;
        }
        
        scale(zoom);
        rotateXYZ((float)Math.toRadians(rotation.x), (float)Math.toRadians(rotation.y), (float)Math.toRadians(rotation.z));
        translate(-position.x, -position.y, -position.z);
        
        return this;
    }
    
    public static class Tether {
        
        public final Entity target;
        public float distance = .5f;
        
        public Tether(Entity target) {
            this.target = target;
        }
    }
}
