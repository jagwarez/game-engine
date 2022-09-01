/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

import jagwarez.game.asset.Color;
import jagwarez.game.asset.Model;
import jagwarez.game.pipeline.SkeletonPipeline;
import jagwarez.game.pipeline.TerrainPipeline;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 *
 * @author jacob
 */
public class Graphics {
    
    private final Game game;
    private final Window window;
    private final World world;
    
    private final Color color = new Color(0.1f,0.1f,.5f,1f);
    
    private Pipeline terrainPipeline;
    private Pipeline modelPipeline;
    private Pipeline skeletonPipeline;
    
    public Graphics(Game game) {
        Game.log("Creating graphics");
        this.game   = game;
        this.window = game.window;
        this.world  = game.world;
    }
    
    public Color color() {
        return color;
    }
    
    public void color(float r, float g, float b, float a) {
        color.r = r; color.g = g; color.b = b; color.a = a;
    }
    
    protected void initialize() throws Exception {
        Game.log("Initializig graphics");
        glClearColor(color.r, color.g, color.b, color.a);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        
        if(false)
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        
        //modelPipeline = new ModelPipeline(game.assets.models).load();
        skeletonPipeline = new SkeletonPipeline(game.assets.models).load();
        terrainPipeline = new TerrainPipeline(world.terrain, world.player).load();

    }
    
    protected void render() throws Exception {
         
        world.entities.sort((Entity a, Entity b) -> {
            if(a.model != null && b.model != null) 
                return Integer.compare(b.model.bones.size(), a.model.bones.size());
            else if(a.model != null)
                return -1;
            else if(b.model != null)
                return 1;
            else return 0;
        });
         
        world.setPerspective((float) Math.toRadians(70), (float) window.width / window.height, 0.1f, 1000.0f);
        world.mul(world.camera.transform());
        
        glViewport(0, 0, window.width, window.height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        terrainPipeline.render(world.terrain, world);
        
        render(world.player);
        
        for(Entity entity : world.entities)
            render(entity);
        
        window.swap();
    }
    
    private void render(Entity entity) throws Exception {
        
        Model model = entity.model;
            
        if(model != null) {

            world.mul(entity.transform(), entity);

            if(entity.model.animated())
                entity.animate();

            //System.out.println("Entity=\n"+entity);

            if(model.skeletal()) {
                skeletonPipeline.render(model, entity);
            } //else
                //modelPipeline.render(model, entity);
        }
    }
    
    public void destroy() {
        
        //modelPipeline.destroy();
        skeletonPipeline.destroy();
        terrainPipeline.destroy();
        
        Game.log("Graphics destroyed");
    }
}
