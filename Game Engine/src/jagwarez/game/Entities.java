/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

import jagwarez.game.asset.Mesh;
import jagwarez.game.asset.Model;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jacob
 */
public class Entities extends ArrayList<Entity> {
    
    public final List<Model> objects;
    public final List<Model> animations;
    
    private int objectVertex = 0;
    private int animationVertex = 0;
    
    public Entities(List<Model> models) {
        
        objects = new ArrayList<>();
        animations = new ArrayList();
        
        for(Model model : models) {
            
            int vertexCount = 0;
            for(Mesh mesh : model.meshes.values())
                    for(Mesh.Group group : mesh.groups)
                        vertexCount += group.vertices.size();
            if(model.animated()) {
                animations.add(model);
                animationVertex += vertexCount;
            } else {
                objects.add(model);
                objectVertex += vertexCount;
            }
        }
    }
    

}
