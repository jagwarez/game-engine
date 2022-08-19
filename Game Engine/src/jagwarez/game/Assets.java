/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagwarez.game;

import jagwarez.game.asset.Mesh;
import jagwarez.game.asset.Model;
import jagwarez.game.asset.reader.AssetReader;
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
            if(model.bones.isEmpty()) {
                modelMap.put(model.name, model);
                for(Mesh mesh : model.meshes.values()) {
                    for(Mesh.Group group : mesh.groups)
                        modelVertex += group.vertices.size();
                }
            } else {
                animationMap.put(model.name, model);
                for(Mesh mesh : model.meshes.values()) {
                    for(Mesh.Group group : mesh.groups)
                        animationVertex += group.vertices.size();
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


