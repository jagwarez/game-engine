/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagwarez.game;

import jagwarez.game.asset.Texture;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jake
 */
public class Terrain {
    
    public final List<Texture> surfaces;
    public final Patch[][] grid;
    public final float scale = 1f;
    public float width, length;
    public int rows, columns;
    
    public Terrain() {
        this(9, 9);
    }
    
    public Terrain(int rows, int columns) {
        
        surfaces = new ArrayList<>();
        grid = new Patch[rows][columns];
        
        for(int x = 0; x <  rows; x++)
            for(int y = 0; y < columns; y++)
                grid[x][y] = new Patch(x, y, scale);
        
        this.rows = rows;
        this.columns = columns;
        this.width = Patch.SIZE*rows*scale;
        this.length = Patch.SIZE*columns*scale;
    }
    
    public static class Patch {
        
        public static final int SIZE = 384;
        public static final int WIDTH = SIZE-1;
        public static final int INDEX_COUNT = (SIZE-1)*(SIZE-1)*6;
        public static final int VERTEX_COUNT = (SIZE)*(SIZE)*3;
        
        public final int row;
        public final int col;
        public final float x;
        public final float y;

        public Texture heightmap = null;
        //public final Texture surfaceMap;
        
        public Patch(int row, int col, float scale) {
            this.row = row;
            this.col = col;
            this.x = row * WIDTH * scale;
            this.y = col * WIDTH * scale;
        }
    }
}
