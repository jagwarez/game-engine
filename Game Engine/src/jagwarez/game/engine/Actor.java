package jagwarez.game.engine;

import jagwarez.game.asset.model.Animation;
import org.joml.Vector3f;
import org.joml.Vector3i;

/**
 *
 * @author jacob
 */
public class Actor extends Entity {
    
    public float speed = .2f;
    
    private final Vector3i movement;
    private final Vector3f direction;
    private Animation animation = null;
    private long time = 0L;
    
    public Actor(String name) {
        super(name);
        movement = new Vector3i(0);
        direction = new Vector3f();
    }
    
    @Override
    public Entity update() {   
        
        float rx = (float) Math.toRadians(rotation.x);
        float ry = (float) Math.toRadians(rotation.y);
        direction.x = (float) (Math.sin(ry)*Math.cos(rx));
        direction.y = (float) Math.sin(rx);
        direction.z = (float) (Math.cos(ry)*Math.cos(rx));
        direction.normalize();
        
        if(movement.z == 1)
            position.add(direction.mul(speed));
        else if(movement.z == -1)
            position.sub(direction.mul(speed));
        
        if(movement.x == 1)
            position.add(direction.cross(World.UP).normalize().mul(speed));
        else if(movement.x == -1)
            position.sub(direction.cross(World.UP).normalize().mul(speed));
        
        movement.set(0);
        
        return super.update();
    }
    
    public void forward() {
        movement.z = 1;
    }
    
    public void backward() {
        movement.z = -1;
    }
    
    public void right() {
        movement.x = 1;
    }
    
    public void left() {
        movement.x = -1;
    }
    
    public void animation(String name) {
        if(animation == null || !animation.name.equals(name)) {
            animation = model.animations.get(name);
            time = Game.time();
        }
    }
    
    public void animate() {
        if(model != null && animation != null)
            model.animate(animation, ((float)(Game.time()-time)/1000f));
    }
    
}
