/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagwarez.game;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 *
 * @author Jake
 */
public class Window {
    
    protected long id;
    protected String title;
    public int width;
    public int height;
    private boolean resize;
    private boolean visible;
    
    protected Window(Settings settings) {
        Game.log("Creating window");
        this.title = settings.title();
        this.width = settings.width();
        this.height = settings.height();
        this.resize = settings.resize();
        this.visible = settings.visible();
    }
    
    public void initialize() {
        
        Game.log("Initializing window");
        
         // the window will be resizable
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_VISIBLE, visible ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resize ? GLFW_TRUE : GLFW_FALSE);

        id = glfwCreateWindow(width, height, title, NULL, NULL);
        if (id == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFWVidMode video = glfwGetVideoMode(glfwGetPrimaryMonitor());
        
        glfwSetWindowMonitor(id, glfwGetPrimaryMonitor(), 0, 0, this.width, this.height, GLFW_DONT_CARE);

        //if(video != null)
            //glfwSetWindowPos(id, (video.width()-this.width)/2, (video.height()-this.height)/2);
        
        glfwSetWindowSizeCallback(id, (long win, int w, int h) -> {
            Window.this.width = w;
            Window.this.height = h;
        });

        glfwMakeContextCurrent(id);
        
        // v-sync
        glfwSwapInterval(1);
        
        // important
        GL.createCapabilities();
        
    }
    
    public int width() {
        return width;
    }
    
    public int height() {
        return height;
    }
    
    protected void fullscreen() {
        
    }
    
    protected void show() {
        glfwShowWindow(id);
    }
    
    protected boolean open() {
        return !glfwWindowShouldClose(id);
    }
    
    protected void swap() {
        glfwSwapBuffers(id);
    }
    
    public void close() {
        Game.log("Closing window");
        glfwSetWindowShouldClose(id, true);
    }
    
    protected void destroy() {
        
        glfwFreeCallbacks(id);
        glfwDestroyWindow(id);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        
        Game.log("Window destroyed");
    }
}
