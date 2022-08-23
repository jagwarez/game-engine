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
    public final float scale = .5f;
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
                grid[x][y] = new Patch(x, y);
        
        this.rows = rows;
        this.columns = columns;
        this.width = Patch.SIZE*rows*scale;
        this.length = Patch.SIZE*columns*scale;
    }
    
    public Patch[][] region(int x, int y) {
        Patch[][] region = new Patch[3][3];
        
        x %= width;
        y %= length;
        
        for(int nx = -1; nx < 2; nx++) {
            int posX = x + nx < 0 ? rows-1 : x + nx > rows ? 0 : x + nx;
            for(int ny = -1; ny < 2; ny++) {
                int posY = y + ny < 0 ? columns-1 : y + ny > columns ? 0 : y + ny;
                region[nx+1][ny+1] = grid[posX][posY];
            }
        }

        return region;
    }
    
    public static class Patch {
        
        public static final int SIZE = 512;
        public static final int INDEX_COUNT = (SIZE)*(SIZE)*6;
        public static final int VERTEX_COUNT = (SIZE+1)*(SIZE+1)*3;
        
        public final int x;
        public final int y;

        //public final Texture heightMap;
        //public final Texture surfaceMap;
        
        public Patch(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
