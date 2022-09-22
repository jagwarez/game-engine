package jagwarez.game.engine;

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
        //position.x = target.position.x;
        //position.y = target.position.y + .5f;
        //position.z = target.position.z - tether.distance;

    }
    
    @Override
    public Entity update() {
        
        identity();
        
        scale(zoom);
        rotateXYZ((float)Math.toRadians(rotation.x), (float)Math.toRadians(rotation.y+180), (float)Math.toRadians(rotation.z));
        translate(0, -(tether.target.position.y+.1f), tether.distance);
        
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
