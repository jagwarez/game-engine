/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.pipeline;

import jagwarez.game.Buffer;
import jagwarez.game.Pipeline;
import jagwarez.game.Program;
import jagwarez.game.asset.Texture;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;

/**
 *
 * @author jacob
 */
public abstract class RenderPipeline<A> implements Pipeline {
    
    public final Program program;
    public final Buffer buffer;
    protected final Map<Integer,Texture> textures;
    protected boolean enabled;
    
    public RenderPipeline() {
        this.program = new Program();
        this.buffer = new Buffer();
        this.textures = new HashMap<>();
        this.enabled = false;
    }
    
    @Override
    public void init() throws Exception {
        program.create();
        buffer.create();
    }
    
    @Override
    public void destroy() {
        
        for(Texture texture : textures.values())
            glDeleteTextures(texture.id);
        
        buffer.destroy();

    }
    
    protected void enable() {
        if(!enabled) {
            
            program.enable();

            buffer.bind();
            
            enabled = true;
        }
    }
    
    protected void disable() {
        buffer.unbind();
        program.disable();
        enabled = false;
    }
    
    protected void texture(Texture texture, Map<Integer,Integer> parameters) throws Exception {
        
        if(texture == null)
            return;
        
        texture.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture.id);
        
        BufferedImage image = ImageIO.read(texture.file);
        ByteBuffer imageBuffer = Texture.buffer(image);
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);
        
        for(Entry<Integer,Integer> param : parameters.entrySet())
            glTexParameteri(GL_TEXTURE_2D, param.getKey(), param.getValue());
        
        glBindTexture(GL_TEXTURE_2D, 0);
        
        textures.put(texture.id, texture);
    }
    
}
