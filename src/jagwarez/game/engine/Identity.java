package jagwarez.game.engine;

import org.joml.Matrix4f;

/**
 *
 * @author jacob
 */
public class Identity extends Matrix4f {
    
    private static int ID = 0;
    
    public final int id;
    
    public Identity() {
        this(++ID);
    }
    
    public Identity(int id) {
        this.id = id;
    }
    
}
