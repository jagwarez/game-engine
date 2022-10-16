package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Game;
import jagwarez.game.engine.Sky;
import jagwarez.game.engine.Window;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 *
 * @author jacob
 */
public class GraphicsPipeline extends MultiPipeline {
    
    private Window window;
    private Sky sky;
     
    public GraphicsPipeline() {    
        pipelines.add(new SkyPipeline());
        pipelines.add(new TerrainPipeline());
        pipelines.add(new EntityPipeline());
        pipelines.add(new ActorPipeline());
    }
    
    @Override
    public void init(Game game) throws Exception {
        
        super.init(game);
        
        window = game.window;
        sky = game.world.sky;
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
    }
    
    @Override
    public void execute() throws Exception {
        
        glViewport(0, 0, window.width, window.height);
        glClearColor(sky.color.r, sky.color.g, sky.color.b, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        super.execute();

    }
    
}
