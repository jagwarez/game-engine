/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game;

import jagware.game.asset.Animation;
import jagware.game.asset.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author Dad
 */
public class Entity extends Matrix4f {
    public final String name;
    public final Model model;
    public final Vector3f position;
    public final Vector3f rotation;
    public final Vector3f scale;
    
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
        scale    = new Vector3f(.1f, .1f, .1f);
    }
    
    public Entity transform() {
        identity();
        //scale(scale.x, scale.y, scale.z);
        translate(position.x, position.y, position.z);
        rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));
        
        return this;
    }
    
    public void animation(String name) {
        animation = model.animations.get(name);
        time = Game.time();
    }
    
    public void animate() {
        if(animation != null) {
            //animation.play((float)(Game.time()-time)/1000f);
        } else {
            model.animate(((float)(Game.time()-time)/1000f)*.5f);
        }
    }
}
