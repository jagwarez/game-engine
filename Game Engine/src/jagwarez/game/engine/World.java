package jagwarez.game.engine;

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
    public final List<Entity> entities;
    public final List<Light> lights;
    
    public World(Window window) {
        this.window = window;
        this.sky = new Sky();
        this.terrain = new Terrain();
        this.camera = new Camera();
        this.player = new Player();
        this.actors = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.lights = new ArrayList<>();
    }
    
    public void update() {
        
        setPerspective((float) Math.toRadians(70), (float) window.width / window.height, 0.1f, 1000f);

        mul(camera.update());
        
        mul(sky.update(), sky);
        
        mul(player.update(), player);
        player.animate();
        
        for(Actor actor : actors) {
            mul(actor.update(), actor);
            actor.animate();
        }
    }
    
    public void destroy() { }
}

