package jagwarez.game;

/**
 *
 * @author jacob
 */
public class Player extends Actor {
    
    public Player() {
        super("player");
    }
    
    @Override
    public Player update() {
        
        identity();
        rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y+180), (float) Math.toRadians(rotation.z));
        this.translate(0, position.y, 0);
        scale(scale.x, scale.y, scale.z);
        
        updated = true;
        
        return this;
    }
    
}
