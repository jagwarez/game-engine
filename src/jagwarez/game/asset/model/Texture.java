package jagwarez.game.asset.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jacob
 */
public class Texture extends Effect {
    
    public int id = -1;
    public final Map<Integer,Integer> parameters;
    public final File file;
    public int width = 0;
    public int height = 0;

    public Texture() {
        this(null);
    }
    
    public Texture(int id) {
        this(null);
        this.id = id;   
    }
    
    public Texture(File file) {
        super(Type.TEXTURE);
        this.file = file;
        parameters = new HashMap<>();
    }
    
}
