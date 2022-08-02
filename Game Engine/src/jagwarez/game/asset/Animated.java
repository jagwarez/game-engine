/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.asset;

import org.joml.Matrix4f;

/**
 *
 * @author jacob
 */
public abstract class Animated {
    
    public final String name;
    public final Matrix4f local = new Matrix4f();
    public final Matrix4f transform = new Matrix4f().identity();
    
    public Animated(String name) { 
        this.name = name;
    }
    
    public abstract void animate();
}
