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
    public final Map<String,Animation> animations;
    public final List<Bone> bones;
    
    public Model(String name) {
        this.name = name;
        this.meshes = new HashMap<>();
        this.animations = new HashMap<>();
        this.bones = new ArrayList<>();
    }
    
    public void animate(float time) {
        for(Animation animation : animations.values())
            animation.play(time);
        
        bones.get(0).animate();
    }
    
    public boolean animated() {
        return !animations.isEmpty();
    }
    
    public boolean skeletal() {
        return !bones.isEmpty();
    }
}
