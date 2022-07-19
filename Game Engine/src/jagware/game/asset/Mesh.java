/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

/**
 *
 * @author jacob
 */
public class Mesh extends Animated {
    
    public final List<Vertex> vertices;
    public final List<Triangle> triangles;
    public final Material material;
    
    public Mesh(int index) {
        super(index);
        this.vertices = new ArrayList<>();
        this.triangles = new ArrayList<>();
        this.material = new Material();
    }
    
    @Override
    public void animate() {
        // TODO
    }
}
