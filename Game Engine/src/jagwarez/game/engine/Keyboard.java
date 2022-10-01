package jagwarez.game.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author jacob
 */
public class Keyboard {
    
    public final Map<Key,Action> binds;
    
    private final Window window;
    
    public Keyboard(Window window) { 
        this.window = window;
        this.binds = new HashMap<>();
    }
    
    protected void update() {
        for(Entry<Key,Action> entry : binds.entrySet()) {
            
            Key key = entry.getKey();
            Action action = entry.getValue();
            
            if(glfwGetKey(window.id, key.code) == GLFW_PRESS)
                key.state = key.state == Key.State.DOWN || key.state == Key.State.PRESSED ? Key.State.DOWN : Key.State.PRESSED;
            else if(key.state == Key.State.DOWN || key.state == Key.State.PRESSED)
                key.state = Key.State.RELEASED;
            else
                key.state = Key.State.UP;
                   
            if(key.state != Key.State.UP && action != null)
                entry.getValue().accept(key);
        }
    }
    
    public interface Action extends Consumer<Key> { };
    
    public static class Key {
        
        public enum State {
            UP,
            DOWN,
            PRESSED,
            RELEASED
        }
        
       public static final Key _SPACE = new Key(GLFW_KEY_SPACE);
       public static final Key _APOSTROPHE = new Key(GLFW_KEY_APOSTROPHE);
       public static final Key _COMMA = new Key(GLFW_KEY_COMMA);
       public static final Key _MINUS = new Key(GLFW_KEY_MINUS);
       public static final Key _PERIOD = new Key(GLFW_KEY_PERIOD);
       public static final Key _SLASH = new Key(GLFW_KEY_SLASH);
       public static final Key _0 = new Key(GLFW_KEY_0);
       public static final Key _1 = new Key(GLFW_KEY_1);
       public static final Key _2 = new Key(GLFW_KEY_2);
       public static final Key _3 = new Key(GLFW_KEY_3);
       public static final Key _4 = new Key(GLFW_KEY_4);
       public static final Key _5 = new Key(GLFW_KEY_5);
       public static final Key _6 = new Key(GLFW_KEY_6);
       public static final Key _7 = new Key(GLFW_KEY_7);
       public static final Key _8 = new Key(GLFW_KEY_8);
       public static final Key _9 = new Key(GLFW_KEY_9);
       public static final Key _SEMICOLON = new Key(GLFW_KEY_SEMICOLON);
       public static final Key _EQUAL = new Key(GLFW_KEY_EQUAL);
       public static final Key _A = new Key(GLFW_KEY_A);
       public static final Key _B = new Key(GLFW_KEY_B);
       public static final Key _C = new Key(GLFW_KEY_C);
       public static final Key _D = new Key(GLFW_KEY_D);
       public static final Key _E = new Key(GLFW_KEY_E);
       public static final Key _F = new Key(GLFW_KEY_F);
       public static final Key _G = new Key(GLFW_KEY_G);
       public static final Key _H = new Key(GLFW_KEY_H);
       public static final Key _I = new Key(GLFW_KEY_I);
       public static final Key _J = new Key(GLFW_KEY_J);
       public static final Key _K = new Key(GLFW_KEY_K);
       public static final Key _L = new Key(GLFW_KEY_L);
       public static final Key _M = new Key(GLFW_KEY_M);
       public static final Key _N = new Key(GLFW_KEY_N);
       public static final Key _O = new Key(GLFW_KEY_O);
       public static final Key _P = new Key(GLFW_KEY_P);
       public static final Key _Q = new Key(GLFW_KEY_Q);
       public static final Key _R = new Key(GLFW_KEY_R);
       public static final Key _S = new Key(GLFW_KEY_S);
       public static final Key _T = new Key(GLFW_KEY_T);
       public static final Key _U = new Key(GLFW_KEY_U);
       public static final Key _V = new Key(GLFW_KEY_V);
       public static final Key _W = new Key(GLFW_KEY_W);
       public static final Key _X = new Key(GLFW_KEY_X);
       public static final Key _Y = new Key(GLFW_KEY_Y);
       public static final Key _Z = new Key(GLFW_KEY_Z);
       public static final Key _LEFT_BRACKET = new Key(GLFW_KEY_LEFT_BRACKET);
       public static final Key _BACKSLASH = new Key(GLFW_KEY_BACKSLASH);
       public static final Key _RIGHT_BRACKET = new Key(GLFW_KEY_RIGHT_BRACKET);
       public static final Key _GRAVE_ACCENT = new Key(GLFW_KEY_GRAVE_ACCENT);
       public static final Key _WORLD_1 = new Key(GLFW_KEY_WORLD_1);
       public static final Key _WORLD_2 = new Key(GLFW_KEY_WORLD_2);
       public static final Key _ESCAPE = new Key(GLFW_KEY_ESCAPE);
       public static final Key _ENTER = new Key(GLFW_KEY_ENTER);
       public static final Key _TAB = new Key(GLFW_KEY_TAB);
       public static final Key _BACKSPACE = new Key(GLFW_KEY_BACKSPACE);
       public static final Key _INSERT = new Key(GLFW_KEY_INSERT);
       public static final Key _DELETE = new Key(GLFW_KEY_DELETE);
       public static final Key _RIGHT = new Key(GLFW_KEY_RIGHT);
       public static final Key _LEFT = new Key(GLFW_KEY_LEFT);
       public static final Key _DOWN = new Key(GLFW_KEY_DOWN);
       public static final Key _UP = new Key(GLFW_KEY_UP);
       public static final Key _PAGE_UP = new Key(GLFW_KEY_PAGE_UP);
       public static final Key _PAGE_DOWN = new Key(GLFW_KEY_PAGE_DOWN);
       public static final Key _HOME = new Key(GLFW_KEY_HOME);
       public static final Key _END = new Key(GLFW_KEY_END);
       public static final Key _CAPS_LOCK = new Key(GLFW_KEY_CAPS_LOCK);
       public static final Key _SCROLL_LOCK = new Key(GLFW_KEY_SCROLL_LOCK);
       public static final Key _NUM_LOCK = new Key(GLFW_KEY_NUM_LOCK);
       public static final Key _PRINT_SCREEN = new Key(GLFW_KEY_PRINT_SCREEN);
       public static final Key _PAUSE = new Key(GLFW_KEY_PAUSE);
       public static final Key _F1 = new Key(GLFW_KEY_F1);
       public static final Key _F2 = new Key(GLFW_KEY_F2);
       public static final Key _F3 = new Key(GLFW_KEY_F3);
       public static final Key _F4 = new Key(GLFW_KEY_F4);
       public static final Key _F5 = new Key(GLFW_KEY_F5);
       public static final Key _F6 = new Key(GLFW_KEY_F6);
       public static final Key _F7 = new Key(GLFW_KEY_F7);
       public static final Key _F8 = new Key(GLFW_KEY_F8);
       public static final Key _F9 = new Key(GLFW_KEY_F9);
       public static final Key _F10 = new Key(GLFW_KEY_F10);
       public static final Key _F11 = new Key(GLFW_KEY_F11);
       public static final Key _F12 = new Key(GLFW_KEY_F12);
       public static final Key _F13 = new Key(GLFW_KEY_F13);
       public static final Key _F14 = new Key(GLFW_KEY_F14);
       public static final Key _F15 = new Key(GLFW_KEY_F15);
       public static final Key _F16 = new Key(GLFW_KEY_F16);
       public static final Key _F17 = new Key(GLFW_KEY_F17);
       public static final Key _F18 = new Key(GLFW_KEY_F18);
       public static final Key _F19 = new Key(GLFW_KEY_F19);
       public static final Key _F20 = new Key(GLFW_KEY_F20);
       public static final Key _F21 = new Key(GLFW_KEY_F21);
       public static final Key _F22 = new Key(GLFW_KEY_F22);
       public static final Key _F23 = new Key(GLFW_KEY_F23);
       public static final Key _F24 = new Key(GLFW_KEY_F24);
       public static final Key _F25 = new Key(GLFW_KEY_F25);
       public static final Key _KP_0 = new Key(GLFW_KEY_KP_0);
       public static final Key _KP_1 = new Key(GLFW_KEY_KP_1);
       public static final Key _KP_2 = new Key(GLFW_KEY_KP_2);
       public static final Key _KP_3 = new Key(GLFW_KEY_KP_3);
       public static final Key _KP_4 = new Key(GLFW_KEY_KP_4);
       public static final Key _KP_5 = new Key(GLFW_KEY_KP_5);
       public static final Key _KP_6 = new Key(GLFW_KEY_KP_6);
       public static final Key _KP_7 = new Key(GLFW_KEY_KP_7);
       public static final Key _KP_8 = new Key(GLFW_KEY_KP_8);
       public static final Key _KP_9 = new Key(GLFW_KEY_KP_9);
       public static final Key _KP_DECIMAL = new Key(GLFW_KEY_KP_DECIMAL);
       public static final Key _KP_DIVIDE = new Key(GLFW_KEY_KP_DIVIDE);
       public static final Key _KP_MULTIPLY = new Key(GLFW_KEY_KP_MULTIPLY);
       public static final Key _KP_SUBTRACT = new Key(GLFW_KEY_KP_SUBTRACT);
       public static final Key _KP_ADD = new Key(GLFW_KEY_KP_ADD);
       public static final Key _KP_ENTER = new Key(GLFW_KEY_KP_ENTER);
       public static final Key _KP_EQUAL = new Key(GLFW_KEY_KP_EQUAL);
       public static final Key _LEFT_SHIFT = new Key(GLFW_KEY_LEFT_SHIFT);
       public static final Key _LEFT_CONTROL = new Key(GLFW_KEY_LEFT_CONTROL);
       public static final Key _LEFT_ALT = new Key(GLFW_KEY_LEFT_ALT);
       public static final Key _LEFT_SUPER = new Key(GLFW_KEY_LEFT_SUPER);
       public static final Key _RIGHT_SHIFT = new Key(GLFW_KEY_RIGHT_SHIFT);
       public static final Key _RIGHT_CONTROL = new Key(GLFW_KEY_RIGHT_CONTROL);
       public static final Key _RIGHT_ALT = new Key(GLFW_KEY_RIGHT_ALT);
       public static final Key _RIGHT_SUPER = new Key(GLFW_KEY_RIGHT_SUPER);
       public static final Key _MENU = new Key(GLFW_KEY_MENU);
       public static final Key _LAST = new Key(GLFW_KEY_LAST);
        
        protected int code;
        private Key(int code) { this.code = code; }
        
        public State state = State.UP;
        public boolean up() { return state == State.UP || state == State.RELEASED; }
        public boolean down() { return state == State.DOWN || state == State.PRESSED; }
        public boolean pressed() { return state == State.PRESSED; }
        public boolean released() { return state == State.RELEASED; }
    }
    
}
