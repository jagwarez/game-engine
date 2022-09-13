/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

import jagwarez.game.asset.Color;
import org.joml.Vector3f;

/**
 *
 * @author jacob
 */
public class Light {
    
    public final Vector3f position;
    public final Color color;
    
    public Light() {
        position = new Vector3f();
        color = new Color();
    }
}
