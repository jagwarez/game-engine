package jagwarez.game;

import jagwarez.game.pipeline.ActorPipeline;
import jagwarez.game.pipeline.SkyPipeline;
import jagwarez.game.pipeline.TerrainPipeline;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
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
public class Graphics implements Pipeline {
    
    public final SkyPipeline sky;
    public final TerrainPipeline terrain;
    //public final EntityPipeline entities;
    public final ActorPipeline actors;
    
    private final Game game;
    private final Window window;
    private final World world;
    
    private boolean wireframe = false;
    
    public Graphics(Game game) {
        this.game   = game;
        this.window = game.window;
        this.world  = game.world;
        
        sky = new SkyPipeline(game);
        terrain = new TerrainPipeline(game);
        //entities = new EntityPipeline(game);
        actors = new ActorPipeline(game);
    }
    
    @Override
    public void init() throws Exception {
        sky.init();
        terrain.init();
        actors.init();
    }
    
    @Override
    public void load() throws Exception {

        glClearColor(world.sky.color.r, world.sky.color.g, world.sky.color.b, 1f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        
        if(false)
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        
        sky.load();
        terrain.load();
        //entities.load();
        actors.load();
        
    }
    
    @Override
    public void render() throws Exception {
        
        glViewport(0, 0, window.width, window.height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        terrain.render();
        
        actors.render();
        
        sky.render();

    }
    
    public void wireframe() {
        wireframe = !wireframe;
        glPolygonMode(GL_FRONT_AND_BACK, wireframe ? GL_LINE : GL_FILL);
    }
    
    @Override
    public void destroy() {
        
        sky.destroy();
        terrain.destroy();
        actors.destroy();
        
        Game.log("Graphics destroyed");
    }
    
    public static class Pipelines {
        
    }
}
