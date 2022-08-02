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
    public Camera() {
        super("camera");
    }
    
    public Entity transform() {
        identity();
        
        rotateXYZ((float)Math.toRadians(rotation.x), (float)Math.toRadians(rotation.y), (float)Math.toRadians(rotation.z));
        translate(-position.x, -position.y, -position.z);
        
        return this;
    }
}
