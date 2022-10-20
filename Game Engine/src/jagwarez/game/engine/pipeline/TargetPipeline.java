package jagwarez.game.engine.pipeline;

import jagwarez.game.asset.model.Texture;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Mouse;
import jagwarez.game.engine.Program;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Window;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL30.*;

/**
 *
 * @author jacob
 */
public class TargetPipeline extends TexturePipeline implements SharedPipeline {
    
    private static final FloatBuffer SAMPLE = BufferUtils.createFloatBuffer(4);
    
    private Window window;
    private Mouse mouse;
    
    private final Program terrainProgram;
    private final Program entityProgram;
    private final Program actorProgram;
    
    private TerrainPipeline terrainPipeline = null;  
    private EntityPipeline entityPipeline = null;
    private ActorPipeline actorPipeline = null;
    
    private int fboId = -1;
    private Texture identityTexture = null;
    private Texture depthTexture = null;
    
    public TargetPipeline() {
        terrainProgram = new Program();
        entityProgram = new Program();
        actorProgram = new Program();
    }
    
    @Override
    public void init(Game game) throws Exception {
        window = game.window;
        mouse = game.mouse;
        
        terrainProgram.init();
        entityProgram.init();
        actorProgram.init();
        
        terrainPipeline = (TerrainPipeline) pipelines.get(TerrainPipeline.class);
        entityPipeline = (EntityPipeline) pipelines.get(EntityPipeline.class);
        actorPipeline = (ActorPipeline) pipelines.get(ActorPipeline.class);
    }
    
    @Override
    public void load() throws Exception {
        
        fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        
        identityTexture = texture(new Texture(glGenTextures()));

        glBindTexture(GL_TEXTURE_2D, identityTexture.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA32F, window.width, window.height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer)null);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, identityTexture.id, 0);
        
        depthTexture = texture(new Texture(glGenTextures()));
        
        glBindTexture(GL_TEXTURE_2D, depthTexture.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, window.width, window.height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture.id, 0);
        
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(status != GL_FRAMEBUFFER_COMPLETE)
            throw new Exception("Framebuffer error");
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
        Shader identityShader = new Shader("jagwarez/game/engine/pipeline/program/target/identity-fs.glsl", Shader.Type.FRAGMENT);
        
        terrainProgram.attach(new Shader("jagwarez/game/engine/pipeline/program/target/terrain-vs.glsl", Shader.Type.VERTEX));
        terrainProgram.attach(identityShader);
        terrainProgram.attribute(0, "position");
        terrainProgram.fragment(0, "color");
        terrainProgram.link();
        
        entityProgram.attach(new Shader("jagwarez/game/engine/pipeline/program/target/entity-vs.glsl", Shader.Type.VERTEX));
        entityProgram.attach(identityShader);
        entityProgram.attribute(0, "position");
        entityProgram.fragment(0, "color");
        entityProgram.link();
        
        actorProgram.attach(new Shader("jagwarez/game/engine/pipeline/program/target/actor-vs.glsl", Shader.Type.VERTEX));
        actorProgram.attach(identityShader);
        actorProgram.attribute(0, "position");
        actorProgram.attribute(1, "bones");
        actorProgram.attribute(2, "weights");
        actorProgram.fragment(0, "color");
        actorProgram.link();
    }

    @Override
    public void execute() throws Exception {
        
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        
        glClearColor(0f, 0f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        render(terrainPipeline, terrainProgram, 0);        
        
        render(entityPipeline, entityProgram, 0);
        
        render(actorPipeline, actorProgram, 2);
        
        glDrawBuffer(GL_NONE);
        
        glReadBuffer(GL_COLOR_ATTACHMENT0);
        
        int mx = mouse.cursor.x;
        int my = window.height-mouse.cursor.y;
        
        glReadPixels(mx, my, 1, 1, GL_RGBA, GL_FLOAT, SAMPLE);
        
        float id = SAMPLE.get(0);
        float ex = SAMPLE.get(1);
        float ey = SAMPLE.get(2);
        float ez = SAMPLE.get(3);
        
        mouse.target.set((int)id, ex, ey, ez);
        
        glReadBuffer(GL_NONE);
        
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    private void render(RenderPipeline pipeline, Program program, int attribs) throws Exception {   
        program.enable();
        
        pipeline.buffer.bind(attribs);
            
        pipeline.render(program);
            
        pipeline.buffer.unbind();
        
        program.disable();
    }
    
    @Override
    public void destroy() throws Exception {
        super.destroy();
        if(fboId != -1)
            glDeleteFramebuffers(fboId);
    }
    
}
