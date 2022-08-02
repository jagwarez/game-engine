/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagwarez.game.asset;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jake
 */
public class Terrain {
    
    public final List<Texture> surfaces;
    public final List<Patch> patches;
    
    public Terrain() {
        surfaces = new ArrayList<>();
        patches = new ArrayList<>();
    }
    
    public static class Patch {
        public final Texture heightMap;
        public final Texture surfaceMap;
        
        public Patch(Texture heightMap, Texture surfaceMap) {
            this.heightMap = heightMap;
            this.surfaceMap = surfaceMap;
        }
    }
}
