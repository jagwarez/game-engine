package jagwarez.game.engine;

import jagwarez.game.asset.model.Color;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 *
 * @author jacob
 */
public class Sky extends Entity {
    
    public final Color color;
    
    public Sky() {
        super("sky");
        color = new Color(0.001f,0.001f,0.005f,1f);
        scale.set(500f);
    }
    
    @Override
    public Entity update() {
        
        glClearColor(color.r, color.g, color.b, 1f);
    
        if(Time.current() % 1f == 0)
            rotation.y += .005f;
        
        identity();
        m31(-50);
        rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y+180), (float) Math.toRadians(rotation.z));
        scale(scale.x, scale.y, scale.z);
        
        return this;
    }
}
