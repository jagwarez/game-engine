package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Game;
import jagwarez.game.engine.Pipeline;
import jagwarez.game.engine.Window;
import jagwarez.game.engine.World;
import java.util.ArrayList;
import java.util.List;
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
public class GraphicsPipeline implements Pipeline {
    
    public final List<Pipeline> pipelines;

    private Window window;
    private World world;
     
    public GraphicsPipeline() {
        
        pipelines = new ArrayList<>();
        
        pipelines.add(new ActorPipeline());
        pipelines.add(new TerrainPipeline());
        pipelines.add(new SkyPipeline());
    }
    
    @Override
    public void init(Game game) throws Exception {
        
        window = game.window;
        world  = game.world;
        
        for(Pipeline pipeline : pipelines)
            pipeline.init(game);
    }
    
    @Override
    public void load() throws Exception {

        glClearColor(world.sky.color.r, world.sky.color.g, world.sky.color.b, 1f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        
        if(false)
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        
        for(Pipeline pipeline : pipelines)
            pipeline.load();
        
    }
    
    @Override
    public void render() throws Exception {
        
        glViewport(0, 0, window.width, window.height);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        for(Pipeline pipeline : pipelines)
            pipeline.render();

    }
    
    @Override
    public void destroy() throws Exception {
        
        for(Pipeline pipeline : pipelines)
            pipeline.destroy();
        
        Game.log("Graphics destroyed");
    }

}
