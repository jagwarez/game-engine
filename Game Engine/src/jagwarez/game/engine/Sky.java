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
        color = new Color(0.01f,0.01f,0.05f,1f);
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
