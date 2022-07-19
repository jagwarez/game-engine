/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;

/**
 *
 * @author jacob
 */
public class Mouse {
    
    private final Window window;
    private int x,y;
    
    public Mouse(Game game) {
        this.window = game.window;
    }
    
    protected void initialize() {
         glfwSetCursorPosCallback(window.id, (win, nx, ny) -> {
            this.x = (int)nx; this.y = (int)ny;
        });
    }
    
    public int x() { return x; }
    public int y() { return y; }
    
    public boolean pressed(Button button) {
        return glfwGetMouseButton(window.id, button.code) == GLFW_PRESS;
    }
    
    public boolean released(Button button) {
        return glfwGetMouseButton(window.id, button.code) == GLFW_RELEASE;
    }
    
    public enum Button {
        LEFT(GLFW_MOUSE_BUTTON_LEFT),
        MIDDLE(GLFW_MOUSE_BUTTON_MIDDLE),
        RIGHT(GLFW_MOUSE_BUTTON_RIGHT);
        
        private final int code;
        private Button(int code) {
            this.code = code;
        }
    }
}
