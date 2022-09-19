package jagwarez.game;

import jagwarez.game.asset.Model;
import org.joml.Vector3f;

/**
 *
 * @author jacob
 */
public class Entity extends Identity {
    
    public final String name;
    public final Vector3f position;
    public final Vector3f rotation;
    public final Vector3f scale;
    public final Vector3f direction;
    
    public Model model = null;
    private boolean updated = false;

    public Entity(String name) {
        this(name, null);
    }
    
    public Entity(String name, Model model) {
        this.name = name;
        this.model = model;
        position = new Vector3f();
        rotation = new Vector3f();
        scale    = new Vector3f(.01f);
        direction = new Vector3f();
    }
    
    public Entity update() {
        
        identity();
        
        translate(position.x, position.y, position.z);
        rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));
        scale(scale.x, scale.y, scale.z);
        
        updated = true;
                     
        return this;
    }
    
    public Vector3f direction() {
        
        if(updated) {
            float rx = (float) Math.toRadians(rotation.x);
            float ry = (float) Math.toRadians(rotation.y);
            direction.x = (float) (Math.sin(ry)*Math.cos(rx));
            direction.y = (float) Math.sin(rx);
            direction.z = (float) (Math.cos(ry)*Math.cos(rx));
            direction.normalize();
            
            updated = false;
        }
        
        return direction;
    }
   
}
