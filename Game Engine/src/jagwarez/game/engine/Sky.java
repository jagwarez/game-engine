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
        color = new Color(0.13f,0.23f,.38f,1f); //new Color(0.001f,0.001f,0.005f,1f);
        scale.set(Terrain.WIDTH);
        //scale.y *= 1.5;
    }
    
    @Override
    public Entity update() {
        
        if(Time.millis() % 1f == 0)
            rotation.y += .005f;
        
        return super.update();
    }
}
