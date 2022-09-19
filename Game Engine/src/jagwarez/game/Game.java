package jagwarez.game;

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
    public final Physics physics;
    public final Assets assets;
    public final World world;
    public static long time = System.nanoTime();
    
    private boolean init = false;
    private boolean loaded = false;
    
    public abstract void load() throws Exception;
    public abstract void loop() throws Exception;
    
    public static void log(String log) {
        //System.out.println(log);
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
        this.graphics = new Graphics(this);
        this.physics = new Physics(this);
    }
    
    private void init() throws Exception {
        
        log("Initializing game");
        
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        
        GLFWErrorCallback.createPrint(System.err).set(); 
        
        window.init();
        mouse.init();
        keyboard.init();
        graphics.init();
        physics.init();
        
        this.init = true;

    }
    
    public void reload() {
        loaded = false;
    }
    
    public void play() throws Exception {
         
        if(!init)
            init();
        
        if(!loaded) {         
              
            load();
            
            graphics.load();
            physics.load();
            
            loaded = true; 
        }
        
        log("Playing game");
        
        window.show();
        
        do {
            glfwPollEvents();
            
            time = System.nanoTime();
            
            loop();
            
            world.update();
            
            physics.render();
            
            graphics.render();
            
            window.swap();
             
        } while(window.open());
        
    }
    
    public void stop() {
        log("Stopping game");
        window.close();
    }
    
    @Override
    public void close() {
        
        world.destroy();
        physics.destroy();
        graphics.destroy();
        window.destroy();
        
        init = loaded = false;
        
        log("Game closed");
    }

}
