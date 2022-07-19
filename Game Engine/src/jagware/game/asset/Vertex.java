/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

import java.util.HashMap;
import java.util.Map;
import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 *
 * @author jacob
 */
public class Vertex extends Indexed {

    public final Vector4f position;
    public final Vector4f normal;
    public final Vector2f texcoord;
    public final Map<Joint,Float> weights;

    public Vertex(int index) {
        super(index);
        this.position = new Vector4f();
        this.normal = new Vector4f();
        this.texcoord = new Vector2f();
        this.weights = new HashMap<>();
    }
}
