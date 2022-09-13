/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author jacob
 */
public class World extends Matrix4f {
    
    public static final Vector3f UP = new Vector3f(0f, 1f, 0f);
    
    public final Window window;
    public final Sky sky;
    public final Terrain terrain;
    public final Camera camera;
    public final Player player;
    public final List<Entity> entities;
    
    public World(Window window) {
        this.window = window;
        this.sky = new Sky();
        this.terrain = new Terrain();
        this.camera = new Camera();
        this.player = new Player();
        this.entities = new ArrayList<>();
    }
    
    public void update() {
        
        setPerspective((float) Math.toRadians(70), (float) window.width / window.height, 0.1f, 1000f);
        
        camera.update();
        player.update();
        
        for(Entity entity : entities)
            entity.update();

        mul(camera);
        
    }
    
    public void destroy() { }
}

