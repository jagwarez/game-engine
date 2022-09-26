package jagwarez.game.asset.model;

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
        public final List<Integer> indices;
        public final Material material;
        public int offset = 0;
        
        public Group(int index) {
            this.index = index;
            this.vertices = new ArrayList<>();
            this.indices = new ArrayList<>();
            this.material = new Material();
        }
    }
}
