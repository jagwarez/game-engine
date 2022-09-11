/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.pipeline;

import jagwarez.game.Game;
import jagwarez.game.Shader;
import jagwarez.game.Window;
import jagwarez.game.World;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL30.*;

/**
 *
 * @author jacob
 */
public class SelectionPipeline extends BasicPipeline<World> {
    
    private final Window window;
    private int fboId = -1;
    private int objTextureId = -1;
    private int depthTextureId = -1;
    
    public SelectionPipeline(Game game) {
        this.window = game.window;
    }
    
    @Override
    public SelectionPipeline load() throws Exception {
        
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
        
        program.bindShader(new Shader("jagwarez/game/pipeline/program/prerender/vs.glsl", Shader.Type.VERTEX));
        program.bindShader(new Shader("jagwarez/game/pipeline/program/prerender/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindFragment(0, "color");
        
        return this;
    }

    @Override
    public void render(World asset) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
