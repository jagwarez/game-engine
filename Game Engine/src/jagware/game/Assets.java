/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagware.game;

import jagware.game.asset.Mesh;
import jagware.game.asset.Terrain;
import jagware.game.asset.Model;
import jagware.game.asset.reader.AssetReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jake
 */
public class Assets {
    
    public Terrain terrain = null;
    public final Models models = new Models();
    
    protected Assets() { }
    
    public static class Models {
        
        private final Map<String,Model> modelMap;
        public int modelVertex = 0;
        
        private final Map<String,Model> animationMap;
        public int animationVertex = 0;

        public Models() {
            this.modelMap = new HashMap<>();
            this.animationMap = new HashMap<>();
        }

        public void add(Model model) {
            if(model.joints.isEmpty()) {
                modelMap.put(model.name, model);
                for(Mesh mesh : model.meshes.values()) {
                    modelVertex += mesh.vertices.size();
                }
            } else {
                animationMap.put(model.name, model);
                for(Mesh mesh : model.meshes.values()) {
                    animationVertex += mesh.vertices.size();
                }
            }
        }
            
        public void read(AssetReader<Model> reader) throws Exception {
            for(Model model = reader.read(); model != null; model = reader.read())
                add(model);
        }
        
        public Collection<Model> models() {
            return modelMap.values();
        }
        
        public Collection<Model> animations() {
            return animationMap.values();
        }
    }
    
}


