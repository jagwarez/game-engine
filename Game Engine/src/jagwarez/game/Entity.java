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
    
    public float speed = .2f;
    public Model model = null;
    
    private final Vector3f direction;
    private boolean oriented = false;
    
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
        direction   = new Vector3f(0f, 0f, -1);
    }
    
    public void forward() {
        position.sub(direction().mul(speed));
    }
    
    public void backward() {
        position.add(direction().mul(speed));
    }
    
    public void right() {
        position.add(direction().cross(World.UP).normalize().mul(speed));
    }
    
    public void left() {
        position.sub(direction().cross(World.UP).normalize().mul(speed));
    }
    
    public Vector3f direction() {
        if(!oriented) {
            float rx = (float) Math.toRadians(rotation.x);
            float ry = (float) Math.toRadians(rotation.y);
            direction.x = (float) (Math.sin(ry)*Math.cos(rx));
            direction.y = (float) Math.sin(rx);
            direction.z = (float) (Math.cos(ry)*Math.cos(rx));
            direction.normalize();
            
            oriented = true;
        }
        
        return direction;
    }
    
    public Entity transform() {
        
        identity();
        
        translate(position.x, position.y, position.z);
        rotateXYZ((float) Math.toRadians(rotation.x), (float) Math.toRadians(rotation.y), (float) Math.toRadians(rotation.z));
        scale(scale.x, scale.y, scale.z);
        
        oriented = false;
        
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
