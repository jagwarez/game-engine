package jagwarez.game;

import jagwarez.game.asset.Texture;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jacob
 */
public class Terrain {
    
    public final List<Texture> surfaces;
    public final Patch[][] grid;
    public float width, length;
    public int rows, columns;
    
    public Terrain() {
        this(12, 12);
    }
    
    public Terrain(int rows, int columns) {
        
        surfaces = new ArrayList<>();
        grid = new Patch[rows][columns];
        
        for(int x = 0; x <  rows; x++)
            for(int y = 0; y < columns; y++)
                grid[x][y] = new Patch(x, y);
        
        this.rows = rows;
        this.columns = columns;
        this.width = Patch.SIZE*rows;
        this.length = Patch.SIZE*columns;
    }
    
    public Patch patch(float x, float y) {
        int row = (int)Math.floor(x/Patch.WIDTH);
        int col = (int)Math.floor(y/Patch.WIDTH);
        return x >= 0 && x < width && y >= 0 && y < length ? grid[row][col] : null;
    }
    
    public List<Patch> region(float x, float y) {
        List<Patch> region = new ArrayList<>();
        Patch center = patch(x, y);
        
        for(int row = -1; row < 2; row++) {
            
            int patchX = center.row + row;
            
            if(patchX < 0 || patchX >= rows)
                continue;
            
            for(int col = -1; col < 2; col++) {
                
                int patchY = center.column + col;
                
                if(patchY < 0 || patchY >= columns)
                    continue;
                
                region.add(grid[patchX][patchY]);
            }
        }
        
        return region;
    }
    
    public List<Patch> region_new(float x, float y) {
        List<Patch> region = new ArrayList<>();
        Patch center = patch(x, y);
        
        int startX = (x%Patch.WIDTH/Patch.WIDTH) % 1f > .5f ? center.row : center.row-1;
        int startY = (y%Patch.WIDTH/Patch.WIDTH) % 1f > .5f ? center.column : center.column-1;
        System.out.println("x="+x+":"+startX);
        for(int row = 0; row < 2; row++) {
            
            int patchX = startX + row;
            
            if(patchX < 0 || patchX >= rows)
                continue;
            
            for(int col = 0; col < 2; col++) {
                
                int patchY = startY + col;
                
                if(patchY < 0 || patchY >= columns)
                    continue;
                
                System.out.println("adding "+patchX+":"+patchY);
                region.add(grid[patchX][patchY]);
            }
        }
        
        return region;
    }
    
    public static class Patch extends Identity {
        
        public static final int SIZE = 384;
        public static final int WIDTH = SIZE-1;
        public static final int INDEX_COUNT = (SIZE-1)*(SIZE-1)*6;
        public static final int VERTEX_COUNT = (SIZE)*(SIZE)*3;
        
        public final int row;
        public final int column;
        public final float x;
        public final float y;

        public Texture heightmap = null;
        //public final Texture surfaceMap;
        
        
        public final List<Entity> entities;
        
        public Patch(int row, int col) {
            this.row = row;
            this.column = col;
            this.x = row * WIDTH;
            this.y = col * WIDTH;
            this.entities = new ArrayList<>();
            
            translate(x, 0f, y);
        }
    }
}
