/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

import jagwarez.game.asset.Color;
import jagwarez.game.asset.Texture;
import org.joml.Matrix4f;

/**
 *
 * @author jacob
 */
public class Sky extends Matrix4f {
    
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int TOP = 2;
    public static final int BOTTOM = 3;
    public static final int BACK = 4;
    public static final int FRONT = 5;  
    
    public static final float SIZE = 500;
    public static final float[] SKYBOX = {         
        -SIZE,  SIZE, -SIZE,
        -SIZE, -SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,
         SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,

        -SIZE, -SIZE,  SIZE,
        -SIZE, -SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE, -SIZE,
        -SIZE,  SIZE,  SIZE,
        -SIZE, -SIZE,  SIZE,

         SIZE, -SIZE, -SIZE,
         SIZE, -SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,

        -SIZE, -SIZE,  SIZE,
        -SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE, -SIZE,  SIZE,
        -SIZE, -SIZE,  SIZE,

        -SIZE,  SIZE, -SIZE,
         SIZE,  SIZE, -SIZE,
         SIZE,  SIZE,  SIZE,
         SIZE,  SIZE,  SIZE,
        -SIZE,  SIZE,  SIZE,
        -SIZE,  SIZE, -SIZE,

        -SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE,  SIZE,
         SIZE, -SIZE, -SIZE,
         SIZE, -SIZE, -SIZE,
        -SIZE, -SIZE,  SIZE,
         SIZE, -SIZE,  SIZE
    };
    
    public int id = -1;
    public final Color color = new Color(.2f, .2f, .3f, 1f);
    public final Texture[] textures = new Texture[6];
    
}
