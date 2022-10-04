package jagwarez.game.engine;

import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author jacob
 */
public class Mouse {
    
    private final Window window;
    private int x = 0, y = 0;
    
    public Mouse(Window window) {
        this.window = window;
    }
    
    protected void init() {
        glfwSetCursorPosCallback(window.id, (win, nx, ny) -> {
            this.x = (int)nx; this.y = (int)ny;
        });
    }
    
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
