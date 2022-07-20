/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jacob
 */
public class Mesh extends Animated {
    
    public final List<Vertex> vertices;
    public final Material material;
    public int index = 0;
    
    public Mesh(String name) {
        super(name);
        this.vertices = new ArrayList<>();
        this.material = new Material();
    }
    
    @Override
    public void animate() {
        // TODO
    }
}
