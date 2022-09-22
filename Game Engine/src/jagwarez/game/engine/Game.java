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
    public static long time = System.nanoTime();
    
    private boolean init = false;
    private boolean loaded = false;
    
    public abstract void load() throws Exception;
    public abstract void loop() throws Exception;
    
    public static void log(String log) {
        System.out.println(log);
    }
    
    public static long time() {
        return System.currentTimeMillis();
    }
    
    public Game() {
        this(new Settings());
    }
    
    public Game(Settings settings) {
        log("Creating game");
        this.window = new Window(settings);
        this.keyboard = new Keyboard(window);
        this.mouse = new Mouse(window);
        this.world = new World(window);
        this.assets = new Assets();
        this.graphics = new Graphics();
        this.pipelines = new ArrayList<>();
    }
    
    private void init() throws Exception {
        
        if(!init) {
            log("Initializing game");

            if (!glfwInit())
                throw new IllegalStateException("Unable to initialize GLFW");

            GLFWErrorCallback.createPrint(System.err).set(); 

            window.init();
            mouse.init();
            keyboard.init();

            this.init = true;
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
        
        log("Playing game");
        
        window.show();
        
        do {
            glfwPollEvents();
            
            time = System.nanoTime();
            
            loop();
            
            world.update();
            
            for(Pipeline pipeline : pipelines)
                pipeline.render();
            
            window.swap();
             
        } while(window.open());
        
    }
    
    public void stop() {
        log("Stopping game");
        window.close();
    }
    
    @Override
    public void close() throws Exception {
        
        for(Pipeline pipeline : pipelines)
                pipeline.destroy();
        
        world.destroy();
        window.destroy();
        
        init = loaded = false;
        
        log("Game closed");
    }

}
