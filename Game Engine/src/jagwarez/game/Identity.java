/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

import org.joml.Matrix4f;

/**
 *
 * @author jacob
 */
public class Identity extends Matrix4f {
    
    private static int ID = 0;
    
    public final int id;
    
    public Identity() {
        this(++ID);
    }
    
    public Identity(int id) {
        this.id = id;
    }
    
}
