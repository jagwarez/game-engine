package jagwarez.game.engine;

import jagwarez.game.asset.model.Color;
import jagwarez.game.asset.model.Texture;

/**
 *
 * @author jacob
 */
public class Sky extends Identity {
    
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int TOP = 2;
    public static final int BOTTOM = 3;
    public static final int FRONT = 4;
    public static final int BACK = 5;  
    
    public static final float SIZE = Terrain.WIDTH;
    public static final float[] SKYBOX = {         
        -SIZE,  SIZE, -SIZE,
        -SIZE, -SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,
         SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,

        -SIZE, -SIZE,  SIZE,
        -SIZE, -SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE,  SIZE,
        -SIZE, -SIZE,  SIZE,

         SIZE, -SIZE, -SIZE,
         SIZE, -SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,

        -SIZE, -SIZE,  SIZE,
        -SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE, -SIZE,  SIZE,
        -SIZE, -SIZE,  SIZE,

        -SIZE,  SIZE, -SIZE,
         SIZE,  SIZE, -SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
        -SIZE,  SIZE,  SIZE,
        -SIZE,  SIZE, -SIZE,

        -SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE,  SIZE,
         SIZE, -SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE,  SIZE,
         SIZE, -SIZE,  SIZE
    };
    
    public int id = -1;
    public final Color color = new Color(.2f, .2f, .3f, 1f);
    public final Texture cubemap = new Texture();
    public final Texture[] textures = new Texture[6];
    
}
