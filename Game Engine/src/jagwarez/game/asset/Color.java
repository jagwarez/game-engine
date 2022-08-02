/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.asset;

/**
 *
 * @author jacob
 */
public class Color extends Effect {
    
    public float r, g, b, a;
    
    public Color() {
        this(0f,0f,0f,0f);
    }
    
    public Color(float r, float g, float b, float a) {
        super(Type.COLOR);
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
}
