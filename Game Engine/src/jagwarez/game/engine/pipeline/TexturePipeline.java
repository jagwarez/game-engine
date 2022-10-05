package jagwarez.game.engine.pipeline;

import jagwarez.game.asset.model.Texture;
import jagwarez.game.engine.Pipeline;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
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
abstract class TexturePipeline implements Pipeline  {
    
    private final Map<Integer,Texture> textures;
    
    public TexturePipeline() {
        textures = new HashMap<>();
    }
    
    @Override
    public void destroy() throws Exception {
        for(Texture texture : textures.values()) {
            if(texture.id != -1) {
                glDeleteTextures(texture.id);
                texture.id = -1;
            }
        }
    }
    
    protected void texture(Texture texture) throws Exception {
        
        if(texture == null)
            return;
        
        if(texture.id == -1) {
            texture.id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texture.id);

            ByteBuffer imageBuffer = buffer(texture);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, texture.width, texture.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);

            for(Entry<Integer,Integer> param : texture.parameters.entrySet())
                glTexParameteri(GL_TEXTURE_2D, param.getKey(), param.getValue());

            glBindTexture(GL_TEXTURE_2D, 0);
        }
        
        textures.put(texture.id, texture);
    }
    
    private ByteBuffer buffer(Texture texture) throws Exception {
        
        BufferedImage image = ImageIO.read(texture.file);
        
        texture.width = image.getWidth();
        texture.height = image.getHeight();
        
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));             // Blue component
                buffer.put((byte) (0xFF));
            }
        }
        return (ByteBuffer) buffer.flip();
    }
}
