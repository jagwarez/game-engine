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
    public final List<Patch> patches;
    public final float scale = .5f;
    
    public Terrain() {
        surfaces = new ArrayList<>();
        patches = new ArrayList<>();
    }
    
    public static class Patch {
        
        public static final int SIZE = 256;
        public static final int INDEX_COUNT = (SIZE)*(SIZE)*6;
        public static final int VERTEX_COUNT = (SIZE+1)*(SIZE+1)*3;

        public final Texture heightMap;
        public final Texture surfaceMap;
        
        public Patch(Texture heightMap, Texture surfaceMap) {
            this.heightMap = heightMap;
            this.surfaceMap = surfaceMap;
        }
    }
}
