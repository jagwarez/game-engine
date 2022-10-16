package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Buffer;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Program;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Window;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL30.*;

/**
 *
 * @author jacob
 */
public class SelectionPipeline extends TexturePipeline {
    
    private final Program program;
    private Buffer buffer;
    private Window window;
    
    private int fboId = -1;
    private int objTextureId = -1;
    private int depthTextureId = -1;
    
    public SelectionPipeline() {
        this.program = new Program();
    }
    
    @Override
    public void init(Game game) throws Exception {
        window = game.window;
    }
    
    @Override
    public void load() throws Exception {
        
        fboId = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboId);
        
        objTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, objTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, window.width, window.height, 0, GL_RGB, GL_FLOAT, (ByteBuffer)null);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, objTextureId, 0);
        
        depthTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, window.width, window.height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTextureId, 0);
        
        glReadBuffer(GL_NONE);

        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if(status != GL_FRAMEBUFFER_COMPLETE)
            throw new Exception("Framebuffer error");
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        
        program.attach(new Shader("jagwarez/game/pipeline/program/select/vs.glsl", Shader.Type.VERTEX));
        program.attach(new Shader("jagwarez/game/pipeline/program/select/fs.glsl", Shader.Type.FRAGMENT));
        program.attribute(0, "position");
        program.fragment(0, "color");
    }

    @Override
    public void execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
