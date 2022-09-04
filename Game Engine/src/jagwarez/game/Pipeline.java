/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

import jagwarez.game.asset.Texture;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import org.joml.Matrix4f;
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
public abstract class Pipeline<A> {
    
    protected final Program program;
    protected final Buffer buffer;
    protected final Map<Integer,Texture> textures;
    protected boolean enabled;
    
    public abstract Pipeline<A> load() throws Exception;
    public abstract void render(A asset, Matrix4f transform) throws Exception;
    
    protected Pipeline() {
        Game.log("Creating pipeline");
        this.program = new Program();
        this.buffer = new Buffer();
        this.textures = new HashMap<>();
        this.enabled = false;
    }
    
    protected void enable() {
        if(!enabled) {
            
            program.enable();

            buffer.bind();
            
            enabled = true;
        }
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
    
    protected void disable() {
        buffer.unbind();
        program.disable();
        enabled = false;
    }
    
    protected void destroy() {
        
        for(Texture texture : textures.values())
            glDeleteTextures(texture.id);
        
        buffer.destroy();
        
        Game.log("Pipeline destroyed");
    }

}
