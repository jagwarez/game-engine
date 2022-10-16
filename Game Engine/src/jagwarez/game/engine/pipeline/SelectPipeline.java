package jagwarez.game.engine.pipeline;

import jagwarez.game.asset.model.Texture;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Program;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Window;
import static jagwarez.game.engine.pipeline.SharedPipeline.pipelines;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL30.*;

/**
 *
 * @author jacob
 */
public class SelectPipeline extends TexturePipeline {
    
    private final Program terrainProgram;
    private final Program entityProgram;
    private Window window;
    
    private TerrainPipeline terrainPipeline = null;
    private ActorPipeline actorPipeline = null;
    private EntityPipeline entityPipeline = null;
    
    private int fboId = -1;
    private Texture identityTexture = null;
    private Texture depthTexture = null;
    
    public SelectPipeline() {
        terrainProgram = new Program();
        entityProgram = new Program();
    }
    
    @Override
    public void init(Game game) throws Exception {
        window = game.window;
        
        terrainPipeline = (TerrainPipeline) pipelines.get(TerrainPipeline.class);
        actorPipeline = (ActorPipeline) pipelines.get(ActorPipeline.class);
        entityPipeline = (EntityPipeline) pipelines.get(EntityPipeline.class);
    }
    
    @Override
    public void load() throws Exception {
        
        fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        
        identityTexture = new Texture();
        identityTexture.id = glGenTextures();
        texture(identityTexture);
        
        glBindTexture(GL_TEXTURE_2D, identityTexture.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, window.width, window.height, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, identityTexture.id, 0);
        
        depthTexture = new Texture();
        depthTexture.id = glGenTextures();
        texture(depthTexture);
        
        glBindTexture(GL_TEXTURE_2D, depthTexture.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, window.width, window.height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture.id, 0);
        
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(status != GL_FRAMEBUFFER_COMPLETE)
            throw new Exception("Framebuffer error");
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
        Shader identityShader = new Shader("jagwarez/game/pipeline/program/select/identity-fs.glsl", Shader.Type.FRAGMENT);
        
        terrainProgram.attach(new Shader("jagwarez/game/pipeline/program/select/terrain-vs.glsl", Shader.Type.VERTEX));
        terrainProgram.attach(identityShader);
        terrainProgram.attribute(0, "position");
        terrainProgram.fragment(0, "color");
        
        entityProgram.attach(new Shader("jagwarez/game/pipeline/program/select/entity-vs.glsl", Shader.Type.VERTEX));
        entityProgram.attach(identityShader);
        entityProgram.attribute(0, "position");
        entityProgram.fragment(0, "color");
    }

    @Override
    public void execute() throws Exception {
        
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        
        terrainProgram.enable();      
        render(terrainPipeline);        
        terrainProgram.disable();
        
        entityProgram.enable();
        render(actorPipeline);
        render(entityPipeline);
        entityProgram.disable();
        
        glDrawBuffer(GL_NONE);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void render(RenderPipeline pipeline) throws Exception {
        pipeline.buffer.bind(0);
            
        pipeline.render();
            
        pipeline.buffer.unbind();
    }
    
    @Override
    public void destroy() throws Exception {
        super.destroy();
        if(fboId != -1)
            glDeleteFramebuffers(fboId);
    }
    
}
