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
    
    public final List<Group> groups;
    
    public Mesh(String name) {
        super(name);
        this.groups = new ArrayList<>();
    }
    
    @Override
    public void animate() {
        // TODO
    }
    
    public static class Group {
        
        public final int index;
        public final List<Vertex> vertices;
        public final Material material;
        public int offset = 0;
        
        public Group(int index) {
            this.index = index;
            this.vertices = new ArrayList<>();
            this.material = new Material();
        }
    }
}
