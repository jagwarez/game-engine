/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.pipeline;

import jagwarez.game.Entity;
import jagwarez.game.Pipeline;
import jagwarez.game.World;
import jagwarez.game.asset.Model;
import java.util.List;

/**
 *
 * @author jacob
 */
public class EntityPipeline implements Pipeline<Entity> {
    
    private final World world;
    private final ModelPipeline modelPipeline;
    private final AnimationPipeline animationPipeline;

    public EntityPipeline(World world, List<Model> models) {
        this.world = world;
        this.modelPipeline = new ModelPipeline(models);
        this.animationPipeline = new AnimationPipeline(models);
    }
    
    @Override
    public EntityPipeline load() throws Exception {
        
        //modelPipeline.load();
        animationPipeline.load();
        
        return this;
    }

    @Override
    public void render(Entity entity) throws Exception {
        
        Model model = entity.model;
            
        if(model != null) {

            world.mul(entity, entity);

            if(entity.model.animated())
                entity.animate();

            if(model.skeletal()) {
                animationPipeline.render(entity);
            } //else
                //modelPipeline.render(model, entity);
        }
    }

    @Override
    public void destroy() {
        modelPipeline.destroy();
        animationPipeline.destroy();
    }
    
    
}
