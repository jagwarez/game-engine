package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Actor;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Player;
import jagwarez.game.engine.Terrain;
import jagwarez.game.engine.World;
import java.nio.ByteBuffer;
import java.util.Map;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glReadBuffer;
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
        glReadBuffer(GL_COLOR_ATTACHMENT0);
        
        gravity(world.player);
        
        glReadBuffer(GL_NONE);
        glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
        
    }
    
    private void gravity(Actor actor) {
        
        Vector3i quantized = actor.quantize();
        Vector3f fraction = actor.fracion();
        int height = 0;
        
        glReadPixels((terrain.heightmap.width-quantized.x)-2, quantized.z, 2, 2, GL_RGBA, GL_UNSIGNED_BYTE, SAMPLE);

        int sw = SAMPLE.get(0) & 0xFF;
        int se = SAMPLE.get(4) & 0xFF;
        int nw = SAMPLE.get(8) & 0xFF;
        int ne = SAMPLE.get(12) & 0xFF;
        
        //if(fraction.x+fraction.z > 1f)
            //height = se + ((nw - ne)*fraction.x)+((nw - sw)*fraction.z);
        //else
            //height = se + ((sw - se)*fraction.x)+((ne - se)*fraction.z);
        
        height = se;
        
        float y = actor.position.y;// * Terrain.SCALE;
        System.out.println("px="+actor.position.x+",pz="+actor.position.z);
        System.out.println("py="+actor.position.y+",se="+se+",h="+height+",a="+y);
        if(y >= height)
            y -= .5f;
        
        if(y < height)
            y = height;
        
        actor.position.y = y;
        
        //for(int i = 0; i < SAMPLE.capacity(); i++) 
            //System.out.println(i+"="+new Integer(SAMPLE.get(i)&0xFF));
        //System.out.println("1-1="+height1+", 1-2="+height2+", 2-1="+height3+", 2-2="+height4);

        //actor.position.y = SAMPLE.get(0);
            //actor.update();

    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
        if(fboId != -1)
            glDeleteFramebuffers(fboId);
    }
    
}
