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
    public final List<Actor> actors;
    
    public World(Window window) {
        this.window = window;
        this.sky = new Sky();
        this.terrain = new Terrain();
        this.camera = new Camera();
        this.player = new Player();
        this.actors = new ArrayList<>();
    }
    
    public void update() {
        
        setPerspective((float) Math.toRadians(70), (float) window.width / window.height, 0.1f, 1000f);

        mul(camera.update());
        
        sky.identity();
        sky.m31(-20f);
        mul(sky, sky);
        
        float x = 1f-(player.position.x % 1f);
        float z = 1f-(player.position.z % 1f);
        
        terrain.identity();
        terrain.translate(((float)-Terrain.OFFSET)+x, 0f, ((float)-Terrain.OFFSET)+z);
        
        mul(player.update(), player);
        player.animate();
        
        for(Actor actor : actors) {
            mul(actor.update(), actor);
            actor.animate();
        }
    }
    
    public void destroy() { }
}

