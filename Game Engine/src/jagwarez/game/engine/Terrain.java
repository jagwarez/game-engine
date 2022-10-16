package jagwarez.game.engine;

import jagwarez.game.asset.model.Texture;
import org.joml.Vector3i;

/**
 *
 * @author jacob
 */
public class Terrain extends Identity {
    
    public static final int SIZE = 800;
    public static final int SCALE = 100;
    public static final int WIDTH = SIZE-1;
    public static final int OFFSET = SIZE/2;
    public static final int INDEX_COUNT = (SIZE-1)*(SIZE-1)*6;
    public static final int VERTEX_COUNT = (SIZE)*(SIZE)*3;
    
    public Texture heightmap = null;
    public Texture blendmap = null;
    
    private final Camera camera;
    
    public Terrain(Camera camera) {
        this.camera = camera;
    }
    
    public void update() {
        Actor target = camera.target != null ? camera.target : camera;
        Vector3i quantized = target.quantize();
        
        identity();
        translate(quantized.x, 0f, quantized.z);
    }
        
}
