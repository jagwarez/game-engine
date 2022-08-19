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
    
    private Entity tether = null;
    
    public Camera() {
        super("camera");
    }
    
    public void tether(Entity tether) {
        this.tether = tether;
    }
    
    public Entity transform() {
        identity();
        
        if(tether != null) {
            position.x = tether.position.x;
            position.y = tether.position.y + 5f;
            position.z = tether.position.z - 10f;
        }
        
        rotateXYZ((float)Math.toRadians(rotation.x), (float)Math.toRadians(rotation.y), (float)Math.toRadians(rotation.z));
        translate(-position.x, -position.y, -position.z);
        
        return this;
    }
}
