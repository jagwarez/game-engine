package jagwarez.game.asset.model;

import java.util.HashMap;
import java.util.Map;
import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 *
 * @author jacob
 */
public class Vertex {

    public final Vector4f position;
    public final Vector4f normal;
    public final Vector2f coordinate;
    public final Map<Bone,Float> weights;
    public final int index;

    public Vertex(int index) {
        this.index = index;
        this.position = new Vector4f();
        this.coordinate = new Vector2f();
        this.normal = new Vector4f();
        this.weights = new HashMap<>();
    }
    
    public Vertex(int index, Vertex copy) {
        this.index = index;
        this.position = copy.position.get(new Vector4f());
        this.coordinate = copy.coordinate.get(new Vector2f());
        this.normal = copy.normal.get(new Vector4f());
        this.weights = new HashMap<>(copy.weights);
    }
    
    @Override
    public String toString() {
        return String.format("%f,%f,%f;",position.x,position.y,position.z)+
               String.format("%f,%f",coordinate.x,coordinate.y)+
               String.format("%f,%f,%f;",normal.x,normal.y,normal.z);
               
    }
}
