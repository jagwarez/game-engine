package jagwarez.game.engine;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author jacob
 */
public abstract class Game implements AutoCloseable {
    
    public final Window window;
    public final Keyboard keyboard;
    public final Mouse mouse;
    public final Graphics graphics;
    public final List<Pipeline> pipelines;
    public final Assets assets;
    public final World world;
    
    private boolean init = false;
    private boolean loaded = false;
    
    public abstract void load() throws Exception;
    public abstract void update() throws Exception;
    
    public Game() {
        this(new Settings());
    }
    
    public Game(Settings settings) {
        window = new Window(settings);
        keyboard = new Keyboard(window);
        mouse = new Mouse(window);
        world = new World(window);
        assets = new Assets();
        graphics = new Graphics();
        pipelines = new ArrayList<>();
    }
    
    private void init() throws Exception {
        
        if(!init) {

            if (!glfwInit())
                throw new IllegalStateException("Unable to initialize GLFW");

            GLFWErrorCallback.createPrint(System.err).set(); 

            window.init();
            mouse.init();
            keyboard.init();

            init = true;
        }
    }
    
    public void reload() {
        loaded = false;
    }
    
    public void play() throws Exception {
         
        init();
        
        if(!loaded) {         
              
            load();
            
            for(Pipeline pipeline : pipelines) {
                pipeline.init(this);
                pipeline.load();
            }
            
            loaded = true; 
        }
        
        window.show();
        
        do {
            
            glfwPollEvents();
            
            Time.update();
            
            update();
            
            world.update();
            
            for(Pipeline pipeline : pipelines)
                pipeline.render();
            
            window.swap();
             
        } while(window.open());
        
    }
    
    public void stop() {
        window.close();
    }
    
    @Override
    public void close() throws Exception {
        
        for(Pipeline pipeline : pipelines)
                pipeline.destroy();
        
        world.destroy();
        window.destroy();
        
        init = loaded = false;
        
    }

}
