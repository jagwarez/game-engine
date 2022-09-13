
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author jacob
 */
public class SkyboxSplitter {
    
    public static final Map<Integer,String> tiles = new HashMap<>();
    
    static {
        
        tiles.put(1, "top");
        tiles.put(4, "right");
        tiles.put(5, "back");
        tiles.put(6, "left");
        tiles.put(7, "front");
        tiles.put(9, "bottom");
        
    }
    
    public static void main(String[] args) throws Exception {
        File skyboxDir = new File("C:\\Development\\skyboxes\\skybox1\\");
        BufferedImage image = ImageIO.read(new File(skyboxDir, "skybox.png"));
        int tileWidth = image.getWidth()/4;
        int tileHeight = image.getHeight()/3;
        
        for(int tileY = 0; tileY < 3; tileY++) {
            for(int tileX = 0; tileX < 4; tileX++) {
                int tileIndex = (tileY*4)+tileX;
                
                if(tiles.containsKey(tileIndex)) {
                    BufferedImage tile = image.getSubimage(tileX*tileWidth, tileY*tileHeight, tileWidth, tileHeight);
                    ImageIO.write(tile, "png", new File(skyboxDir, tiles.get(tileIndex)+".png"));
                }
            }
        }
    }
}
