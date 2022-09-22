package jagwarez.game.asset.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

/**
 *
 * @author jacob
 */
public class Texture extends Effect {
    
    public int id = -1;
    public final File file;

    public Texture(File file) {
        super(Type.TEXTURE);
        this.file = file;
    }
    
    public static ByteBuffer buffer(BufferedImage image) throws Exception {
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
