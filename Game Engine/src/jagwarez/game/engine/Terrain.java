package jagwarez.game.engine;

import jagwarez.game.asset.model.Texture;

/**
 *
 * @author jacob
 */
public class Terrain extends Identity {
    
    public static final int SIZE = 600;
    public static final int SCALE = 100;
    public static final int WIDTH = SIZE-1;
    public static final int OFFSET = SIZE/2;
    public static final int INDEX_COUNT = (SIZE-1)*(SIZE-1)*6;
    public static final int VERTEX_COUNT = (SIZE)*(SIZE)*3;
    
    public Texture heightmap = null;
    public Texture blendmap = null;
        
}
