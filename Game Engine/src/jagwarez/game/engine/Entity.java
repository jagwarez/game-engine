package jagwarez.game.engine;

import jagwarez.game.asset.model.Model;
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
 
    public Model model = null;

    public Entity(String name) {
        this(name, null);
    }
    
    public Entity(String name, Model model) {
        this.name = name;
        this.model = model;
        position = new Vector3f();
        rotation = new Vector3f();
        scale    = new Vector3f(1f);
    }
    
    public Entity update() {
        
        identity();
        
        translate(position.x, position.y, position.z);
        rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y+180), (float) Math.toRadians(rotation.z));
        scale(scale.x, scale.y, scale.z);
                     
        return this;
    }
    
}
