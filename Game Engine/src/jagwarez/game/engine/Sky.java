package jagwarez.game.engine;

import jagwarez.game.asset.model.Color;

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
        
        if(Time.current() % 1f == 0)
            rotation.y += .005f;
        
        identity();
        m31(-50);
        rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y+180), (float) Math.toRadians(rotation.z));
        scale(scale.x, scale.y, scale.z);
        
        return this;
    }
}
