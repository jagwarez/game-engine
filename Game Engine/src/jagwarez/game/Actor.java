/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        position.sub(direction().mul(speed));
    }
    
    public void backward() {
        position.add(direction().mul(speed));
    }
    
    public void right() {
        position.sub(direction().cross(World.UP).normalize().mul(speed));
    }
    
    public void left() {
        position.add(direction().cross(World.UP).normalize().mul(speed));
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
