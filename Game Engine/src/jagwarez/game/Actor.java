package jagwarez.game;

import jagwarez.game.asset.Animation;

/**
 *
 * @author jacob
 */
public class Actor extends Entity {
    
    public float speed = .2f;
    
    private Animation animation = null;
    private long time = 0L;
    
    public Actor(String name) {
        super(name);
    }
    
    public void forward() {
        position.add(direction().mul(speed));
    }
    
    public void backward() {
        position.sub(direction().mul(speed));
    }
    
    public void right() {
        position.add(direction().cross(World.UP).normalize().mul(speed));
    }
    
    public void left() {
        position.sub(direction().cross(World.UP).normalize().mul(speed));
    }
    
    public void animation(String name) {
        if(animation == null || !animation.name.equals(name)) {
            animation = model.animations.get(name);
            time = Game.time();
        }
    }
    
    public void animate() {
        if(model != null && animation != null)
            model.animate(animation, ((float)(Game.time()-time)/1000f));
    }
    
}
