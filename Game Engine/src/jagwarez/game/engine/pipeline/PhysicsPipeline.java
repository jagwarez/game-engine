package jagwarez.game.engine.pipeline;

import jagwarez.game.asset.model.Texture;
import jagwarez.game.engine.Buffer;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Player;
import jagwarez.game.engine.Program;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Terrain;
import jagwarez.game.engine.World;
import java.nio.ByteBuffer;
import org.joml.Vector2f;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawBuffer;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RGB32F;
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
    
    private final Program program;
    private Buffer buffer;
    private int fboId = -1;
    private Texture colorBuffer;
    private Texture depthBuffer;
    
    private World world;
    private Terrain terrain;
    private Player player;
    
    public PhysicsPipeline() {
        program = new Program();
        buffer = null;
        colorBuffer = new Texture();
        depthBuffer = new Texture();
    }
    
    @Override
    public void init(Game game) throws Exception {
        program.init();
        buffer = buffers.get(TerrainPipeline.class);
        world = game.world;
        terrain = game.world.terrain;
        player = game.world.player;
    }

    @Override
    public void load() throws Exception {
        
        fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        
        colorBuffer.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, colorBuffer.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, Terrain.SIZE, Terrain.SIZE, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorBuffer.id, 0);    
        texture(colorBuffer);
        
        depthBuffer.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthBuffer.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, Terrain.SIZE, Terrain.SIZE, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthBuffer.id, 0);     
        texture(depthBuffer);
        
        glReadBuffer(GL_NONE);

        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(status != GL_FRAMEBUFFER_COMPLETE)
            throw new Exception("Framebuffer error");
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
        program.bindShader(new Shader("jagwarez/game/engine/pipeline/program/physics/vs.glsl", Shader.Type.VERTEX));
        program.bindShader(new Shader("jagwarez/game/engine/pipeline/program/physics/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindFragment(0, "color");
        
    }

    @Override
    public void process() throws Exception {
        
        program.enable();
        buffer.bind();
        
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fboId);
        
        if(terrain.heightmap != null) {
            
            glActiveTexture(GL_TEXTURE0 + 0);
            glBindTexture(GL_TEXTURE_2D, terrain.heightmap.id);
            
            Vector2f offset = new Vector2f(player.position.x-(float)Terrain.OFFSET, (float)player.position.z-(float)Terrain.OFFSET);

            program.bindUniform("twidth").set1f(terrain.heightmap.width);
            program.bindUniform("offset").set2f(offset.x, offset.y);
            program.bindUniform("use_hmap").setBool(true);
            program.bindUniform("hmap").set1i(0);

        } else {
            program.bindUniform("use_hmap").setBool(false);
        }

        glDrawElements(GL_TRIANGLES, Terrain.INDEX_COUNT, GL_UNSIGNED_INT, 0);
        
        glBindTexture(GL_TEXTURE_2D, 0);
        
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        
        buffer.unbind();
        program.disable();
        
        
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
        if(fboId != -1)
            glDeleteFramebuffers(fboId);
    }
    
}
