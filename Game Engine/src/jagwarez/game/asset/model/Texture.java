package jagwarez.game.asset.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;

/**
 *
 * @author jacob
 */
public class Texture extends Effect {
    
    public int id = -1;
    public final Map<Integer,Integer> parameters;
    public final File file;
    public int width = 0;
    public int height = 0;

    public Texture() {
        this(null);
    }
    
    public Texture(File file) {
        super(Type.TEXTURE);
        this.file = file;
        parameters = new HashMap<>();
    }
    
    public ByteBuffer buffer() throws Exception {
        
        BufferedImage image = ImageIO.read(file);
        
        width = image.getWidth();
        height = image.getHeight();
        
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
