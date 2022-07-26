/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagware.game.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jake
 */
public class Model {
    
    public final String name;
    public final Map<String,Mesh> meshes;
    public final List<Bone> bones;
    public final Map<String,Animation> animations;
   
    public Model(String name) {
        this.name = name;
        this.meshes = new HashMap<>();
        this.bones = new ArrayList<>();
        this.animations = new HashMap<>();
    }
    
    public void animate(Animation animation, float time) {
       animation.play(time);
       if(!bones.isEmpty())
            bones.get(0).animate();
    }
    
    public boolean animated() {
        return !animations.isEmpty();
    }
    
    public boolean skeletal() {
        return !bones.isEmpty();
    }
}
