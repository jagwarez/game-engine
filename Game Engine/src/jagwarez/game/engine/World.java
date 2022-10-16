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
        camera = new Camera();
        terrain = new Terrain(camera);
        sky = new Sky();
        player = new Player();
        actors = new ArrayList<>();
        entities = new ArrayList<>();
        lights = new ArrayList<>();
    }
    
    public void update() {      
        setPerspective((float) Math.toRadians(70), (float) window.width / window.height, 0.1f, 1000f);
    }
    
    public void destroy() { }
}

