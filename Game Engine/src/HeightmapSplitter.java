import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author jacob
 */
public class HeightmapSplitter {
    public static void main(String[] args) throws Exception {
        File skyboxDir = new File("C:\\Development\\Java\\Projects\\game-engine\\Game Engine\\games\\hello\\assets\\terrain");
        BufferedImage image = ImageIO.read(new File(skyboxDir, "terrain.png"));
        int tileWidth = image.getWidth()/4;
        int tileHeight = image.getHeight()/4;
        
        for(int tileX = 3; tileX >= 0 ; tileX--) {
            for(int tileY = 3; tileY >= 0; tileY--) {

                BufferedImage tile = image.getSubimage((tileX*tileWidth)+(3-tileX), (tileY*tileHeight)+(3-tileY), tileWidth, tileHeight);
                ImageIO.write(tile, "png", new File(skyboxDir, (3-tileX)+"-"+(3-tileY)+".png"));
                
            }
        }
    }
}
