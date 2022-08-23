/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

import jagwarez.game.asset.Animation;
import jagwarez.game.asset.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author Dad
 */
public class Entity extends Matrix4f {
    
    private static int ID = 0;
            
    public final int id = ++ID;
    public final String name;
    public final Vector3f position;
    public final Vector3f rotation;
    public final Vector3f scale;
    public Model model;
    
    private Animation animation = null;
    private long time = 0L;
    
    public Entity(String name) {
        this(name, null);
    }
    
    public Entity(String name, Model model) {
        this.name = name;
        this.model = model;
        position = new Vector3f(0f);
        rotation = new Vector3f(0f);
        scale    = new Vector3f(.25f);
    }
    
    public Entity transform() {
        identity();
        
        translate(position.x, position.y, position.z);
        rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));
        scale(scale.x, scale.y, scale.z);
        
        return this;
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
