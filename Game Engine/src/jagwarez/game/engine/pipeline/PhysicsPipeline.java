package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Actor;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Player;
import jagwarez.game.engine.Terrain;
import jagwarez.game.engine.World;
import java.nio.ByteBuffer;
import java.util.List;
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
    private List<Actor> actors;
    
    private static final ByteBuffer SAMPLE = BufferUtils.createByteBuffer(16);
    
    @Override
    public void init(Game game) throws Exception {
        world = game.world;
        terrain = world.terrain;
        player = world.player;
        actors = world.actors;
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
        
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
            throw new Exception("Unable to bind framebuffer");
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
                
    }

    @Override
    public void process() throws Exception {

        glBindFramebuffer(GL_READ_FRAMEBUFFER, fboId);
        glReadBuffer(GL_COLOR_ATTACHMENT0);
        
        world.camera.update();
        //gravity(world.camera);
        
        physics(world.player);
        
        for(Actor actor : actors)
            physics(actor);
        
        glReadBuffer(GL_NONE);
        glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
        
    }
    
    private void physics(Actor actor) {
        
        Vector3i quantized = actor.quantize();
        Vector3f fraction = actor.fracion();
        float height = 0;
        
        int mapx = terrain.heightmap.width-quantized.x-1;
        int mapy = terrain.heightmap.height-quantized.z-1;
        
        glReadPixels(mapx, mapy, 2, 2, GL_RGBA, GL_UNSIGNED_BYTE, SAMPLE);

        int nw = SAMPLE.get(0) & 0xFF;
        int ne = SAMPLE.get(4) & 0xFF;
        int sw = SAMPLE.get(8) & 0xFF;
        int se = SAMPLE.get(12) & 0xFF;
        
        //System.out.println("mx="+mapx+",mz="+mapy);
        //System.out.println("px="+actor.position.x+",pz="+actor.position.z);
        //System.out.println("qx="+quantized.x+",qz="+quantized.z);
        //System.out.println("fx="+fraction.x+",fz="+fraction.z);
        //System.out.println("sw="+sw+",se="+se+",nw="+nw+",ne="+ne);
        
        if(fraction.x+fraction.z > 1f) {
            float hx = ((float)(ne-nw)*(1-fraction.x));
            float hz = ((float)(sw-nw)*(1-fraction.z));
            //System.out.println("hx(ne-nw)="+hx);
            //System.out.println("hz(sw-nw)="+hz);
            height = nw + (hx + hz);
            
        } else {
            float hx = ((float)(sw-se)*fraction.x);
            float hz = ((float)(ne-se)*fraction.z);
            //System.out.println("hx(sw-se)="+hx);
            //System.out.println("hz(ne-se)="+hz);
            height = se + hx + hz;
        }
        
        //System.out.println("height="+height);
        
        float y = actor.position.y;
        //System.out.println("px="+actor.position.x+",pz="+actor.position.z);
        //System.out.println("py="+actor.position.y+",se="+se+",h="+height+",a="+y);
        if(y > height)
            y -= actor.weight;
        
        if(y < height)
            y = height;
        
        actor.position.y = y;
                
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
        if(fboId != -1)
            glDeleteFramebuffers(fboId);
    }
    
}
