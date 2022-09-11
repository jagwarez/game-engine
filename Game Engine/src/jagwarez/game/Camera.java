/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagwarez.game;

/**
 *
 * @author Jake
 */
public class Camera extends Entity {
    
    public Tether tether = null;
    
    public Camera() {
        super("camera");
    }
    
    public void follow(Entity target) {
        this.tether = new Tether(target);
    }
    
    public Entity transform() {
        identity();
        
        if(tether != null) {
            Entity target = tether.target;
            position.x = target.position.x;
            position.y = target.position.y + 5f;
            position.z = target.position.z - tether.distance;
        }
        
        rotateXYZ((float)Math.toRadians(rotation.x), (float)Math.toRadians(rotation.y), (float)Math.toRadians(rotation.z));
        translate(-position.x, -position.y, -position.z);
        
        return this;
    }
    
    public static class Tether {
        
        public final Entity target;
        public float distance = 5f;
        
        public Tether(Entity target) {
            this.target = target;
        }
    }
}
