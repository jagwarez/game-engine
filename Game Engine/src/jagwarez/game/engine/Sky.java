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
        color = new Color(0.15f,0.2f,0.4f,1f);
    }
    
    @Override
    public Entity update() {
        
        if(Time.current() % 1f == 0)
            rotation.y += .005f;
        
        identity();
        m31(0);
        rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y+180), (float) Math.toRadians(rotation.z));
        scale(scale.x, scale.y, scale.z);
        
        return this;
    }
}
