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
import javax.imageio.ImageIO;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 *
 * @author jacob
 */
public abstract class Pipeline<A> {
    
    protected final Program program;
    protected final Buffer buffer;
    protected final Map<Integer,Texture> textures;
    protected boolean enabled;
    
    protected Pipeline() {
        Game.log("Creating pipeline");
        this.program = new Program();
        this.buffer = new Buffer();
        this.textures = new HashMap<>();
        this.enabled = false;
    }
    
    public abstract Pipeline<A> load() throws Exception;
    public abstract void render(A asset, Matrix4f transform) throws Exception;
    
    protected void enable() {
        if(!enabled) {
            
            program.enable();

            buffer.bind();
            
            enabled = true;
        }
    }
    
    protected void load(Texture texture) throws Exception {
        
        BufferedImage image = ImageIO.read(texture.file);
        ByteBuffer textureBuffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                
                textureBuffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                textureBuffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                textureBuffer.put((byte) (pixel & 0xFF));             // Blue component
                textureBuffer.put((byte) (0xFF));
            }
        }
        
        texture.id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture.id);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer)textureBuffer.flip());
        
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
