package jagwarez.game.engine;

import jagwarez.game.asset.model.Model;
import org.joml.Vector3f;
import org.joml.Vector3i;

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
        rotateXYZ((float) Math.toRadians(rotation.x),
                  (float) Math.toRadians(rotation.y+180),
                  (float) Math.toRadians(rotation.z));
        scale(scale.x, scale.y, scale.z);
                     
        return this;
    }
    
    public Vector3i quantize() {
        return new Vector3i(position.x < 0 ? (int) Math.ceil(position.x) : (int) Math.floor(position.x),
                            position.y < 0 ? (int) Math.ceil(position.y) : (int) Math.floor(position.y),
                            position.z < 0 ? (int) Math.ceil(position.z) : (int) Math.floor(position.z));
    }
    
    public Vector3f fracion() {
        return new Vector3f(position.x % 1f, position.y % 1f, position.z % 1f);
    }
    
}
