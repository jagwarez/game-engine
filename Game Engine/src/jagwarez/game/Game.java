/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagwarez.game;

import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author Jake
 */
public abstract class Game implements AutoCloseable {
    
    public final Window window;
    public final Keyboard keyboard;
    public final Mouse mouse;
    public final Graphics graphics;
    public final Assets assets;
    public final World world;
    public static long time = System.nanoTime();
    
    private boolean initialized = false;
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
    }
    
    private void initialize() throws Exception {
        
        log("Initializing game");
        
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        
        GLFWErrorCallback.createPrint(System.err).set(); 
        
        window.initialize();  
        graphics.initialize();
        mouse.initialize();
        
        this.initialized = true;

    }
    
    public void reload() {
        loaded = false;
    }
    
    public void play() throws Exception {
        
        if(!loaded) {
            load();
            loaded = true; 
        }
        
        if(!initialized)
            initialize();
        
        log("Playing game");
        
        window.show();
        
        do {
            glfwPollEvents();
            
            time = System.nanoTime();
            
            loop();
            
            world.update();
            
            graphics.render();
             
        } while(window.open());
        
    }
    
    public void stop() {
        log("Stopping game");
        window.close();
    }
    
    @Override
    public void close() {
        
        world.destroy();
        graphics.destroy();
        window.destroy();
        
        initialized = loaded = false;
        
        log("Game closed");
    }

}
