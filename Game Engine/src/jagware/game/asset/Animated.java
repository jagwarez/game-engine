/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

import org.joml.Matrix4f;

/**
 *
 * @author jacob
 */
public abstract class Animated extends Indexed {
    
    public final Matrix4f local = new Matrix4f();
    public final Matrix4f transform = new Matrix4f();
    
    public Animated(int index) { 
        super(index); 
    }
    
    public abstract void animate();
}
