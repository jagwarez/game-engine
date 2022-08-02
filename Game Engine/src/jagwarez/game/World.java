/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

import jagwarez.game.asset.Terrain;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

/**
 *
 * @author jacob
 */
public class World extends Matrix4f {
    
    public Terrain[][] map;
    public final Camera camera;
    public final List<Entity> entities;
    
    public World() {
        this.camera = new Camera();
        this.entities = new ArrayList<>();
    }
    
    public void destroy() { }
}

