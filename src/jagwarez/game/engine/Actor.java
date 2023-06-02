package jagwarez.game.engine;

import jagwarez.game.asset.model.Animation;
import jagwarez.game.asset.model.Model;
import org.joml.Vector3f;
import org.joml.Vector3i;

/**
 *
 * @author jacob
 */
public class Actor extends Entity {
    
    public float weight = .7f;
    public float speed = 1.0f;   
    public final Vector3i movement;
    
    private Animation animation = null;
    private long marker = 0l;
    
    public Actor(String name) {
        this(name, null);
    }
    
    public Actor(String name, Model model) {
        super(name, model);
        movement = new Vector3i();
    }
    
    @Override
    public Entity update() {
        
        animate();
        
        move();
        
        return super.update();
    }
    
    public void move() {
        Vector3f direction = direction().mul(speed);
        
        if(movement.x == 1)
            position.add(direction.cross(World.UP, new Vector3f()));
        else if(movement.x == -1)
            position.sub(direction.cross(World.UP, new Vector3f()));

         if(movement.z == 1)
            position.add(direction);
        else if(movement.z == -1)
            position.sub(direction);
         
        movement.set(0);
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
    
    public void up() {
        movement.y = 1;
    }
    
    public void down() {
        movement.y = -1;
    }
    
    public void animation(String name) {
        if(animation == null || !animation.name.equals(name)) {

            animation = model.animations.get(name);
            marker = Time.millis();
            
            if(animation == null)
                model.pose();
        }
    }
    
    public void animate() {
        if(animation != null)
            animation.play(Time.millis()-marker);
    }
    
    protected Vector3f direction() {
        float rx = (float) Math.toRadians(rotation.x);
        float ry = (float) Math.toRadians(rotation.y);
           
        Vector3f direction = new Vector3f();
        direction.x = (float) (Math.sin(ry)*Math.cos(rx));
        direction.y = (float) Math.sin(rx);
        direction.z = (float) (Math.cos(ry)*Math.cos(rx));
        
        return direction.normalize();
    }
    
}
