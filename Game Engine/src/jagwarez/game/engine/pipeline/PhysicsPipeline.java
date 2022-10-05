package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Actor;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Player;
import jagwarez.game.engine.Terrain;
import jagwarez.game.engine.World;
import java.nio.ByteBuffer;
import java.util.Map;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

/**
 *
 * @author jacob
 */
public class PhysicsPipeline extends TexturePipeline implements SharedPipeline {
    
    private int fboId = -1;
    
    private World world;
    private Terrain terrain;
    private Player player;
    
    private static final ByteBuffer SAMPLE = BufferUtils.createByteBuffer(16);
    
    @Override
    public void init(Game game) throws Exception {
        world = game.world;
        terrain = game.world.terrain;
        player = game.world.player;
    }

    @Override
    public void load() throws Exception {
        
        Map<Integer,Integer> parameters = terrain.heightmap.parameters;
        parameters.put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        parameters.put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        parameters.put(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        parameters.put(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        
        texture(terrain.heightmap);
        
        fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        
        glBindTexture(GL_TEXTURE_2D, terrain.heightmap.id);
        glFramebufferTexture2D(GL_READ_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, terrain.heightmap.id, 0);
        
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(status != GL_FRAMEBUFFER_COMPLETE)
            throw new Exception("Unable to bind framebuffer");
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
                
    }

    @Override
    public void process() throws Exception {

        glBindFramebuffer(GL_READ_FRAMEBUFFER, fboId);
        
        gravity(world.player);
        
        glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
        
    }
    
    private void gravity(Actor actor) {
        
        Vector3i quantized = actor.quantize();
        
        if(quantized.x > 0 && quantized.z > 0) {
            glReadPixels(Terrain.SIZE-(quantized.x-1), quantized.z-1, 2, 2, GL_RGBA, GL_UNSIGNED_BYTE, SAMPLE);
            
            System.out.println(SAMPLE.get(0));
            
            //actor.position.y = SAMPLE.get(0);
            //actor.update();
        }
        
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
        if(fboId != -1)
            glDeleteFramebuffers(fboId);
    }
    
}
