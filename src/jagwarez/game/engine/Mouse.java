package jagwarez.game.engine;

import java.nio.DoubleBuffer;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author jacob
 */
public class Mouse {
    
    private static final DoubleBuffer X = BufferUtils.createDoubleBuffer(1);
    private static final DoubleBuffer Y = BufferUtils.createDoubleBuffer(1);
    
    public final Vector2i cursor;
    public final Target target;
    
    private final Window window;
    
    public Mouse(Window window) {
        this.window = window;
        cursor = new Vector2i();
        target = new Target();
    }
    
    protected void init() {
        glfwSetInputMode(window.id, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }
    
    protected void update() {
        glfwGetCursorPos(window.id, X, Y);   
        cursor.set((int)X.get(0),(int)Y.get(0));
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
    
    public static class Target {
        public int id;
        public final Vector3f position = new Vector3f();
        
        public void set(int id, float x, float y, float z) {
            this.id = id;
            this.position.set(x, y, z);
        }
        
        public void clear() {
            id = 0;
            position.set(0);
        }
    }
}
