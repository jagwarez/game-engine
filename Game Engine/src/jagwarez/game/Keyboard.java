package jagwarez.game;

import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author jacob
 */
public class Keyboard {
    
    private final Window window;

    public Keyboard(Window window) { 
        this.window = window;
    }
    
    protected void init() {
        glfwSetKeyCallback(window.id, (win, key, code, action, mods) -> {
            
        });
    }
    
    public boolean pressed(Key key) {
        return glfwGetKey(window.id, key.code) == GLFW_PRESS;
    }
    
    public boolean released(Key key) {
        return glfwGetKey(window.id, key.code) == GLFW_RELEASE;
    }
    
    public boolean down(Key key) {
        return glfwGetKey(window.id, key.code) == GLFW_REPEAT;
    }
    
    public enum Key {
        _SPACE(GLFW_KEY_SPACE),
        _APOSTROPHE(GLFW_KEY_APOSTROPHE),
        _COMMA(GLFW_KEY_COMMA),
        _MINUS(GLFW_KEY_MINUS),
        _PERIOD(GLFW_KEY_PERIOD),
        _SLASH(GLFW_KEY_SLASH),
        _0(GLFW_KEY_0),
        _1(GLFW_KEY_1),
        _2(GLFW_KEY_2),
        _3(GLFW_KEY_3),
        _4(GLFW_KEY_4),
        _5(GLFW_KEY_5),
        _6(GLFW_KEY_6),
        _7(GLFW_KEY_7),
        _8(GLFW_KEY_8),
        _9(GLFW_KEY_9),
        _SEMICOLON(GLFW_KEY_SEMICOLON),
        _EQUAL(GLFW_KEY_EQUAL),
        _A(GLFW_KEY_A),
        _B(GLFW_KEY_B),
        _C(GLFW_KEY_C),
        _D(GLFW_KEY_D),
        _E(GLFW_KEY_E),
        _F(GLFW_KEY_F),
        _G(GLFW_KEY_G),
        _H(GLFW_KEY_H),
        _I(GLFW_KEY_I),
        _J(GLFW_KEY_J),
        _K(GLFW_KEY_K),
        _L(GLFW_KEY_L),
        _M(GLFW_KEY_M),
        _N(GLFW_KEY_N),
        _O(GLFW_KEY_O),
        _P(GLFW_KEY_P),
        _Q(GLFW_KEY_Q),
        _R(GLFW_KEY_R),
        _S(GLFW_KEY_S),
        _T(GLFW_KEY_T),
        _U(GLFW_KEY_U),
        _V(GLFW_KEY_V),
        _W(GLFW_KEY_W),
        _X(GLFW_KEY_X),
        _Y(GLFW_KEY_Y),
        _Z(GLFW_KEY_Z),
        _LEFT_BRACKET(GLFW_KEY_LEFT_BRACKET),
        _BACKSLASH(GLFW_KEY_BACKSLASH),
        _RIGHT_BRACKET(GLFW_KEY_RIGHT_BRACKET),
        _GRAVE_ACCENT(GLFW_KEY_GRAVE_ACCENT),
        _WORLD_1(GLFW_KEY_WORLD_1),
        _WORLD_2(GLFW_KEY_WORLD_2),
        _ESCAPE(GLFW_KEY_ESCAPE),
        _ENTER(GLFW_KEY_ENTER),
        _TAB(GLFW_KEY_TAB),
        _BACKSPACE(GLFW_KEY_BACKSPACE),
        _INSERT(GLFW_KEY_INSERT),
        _DELETE(GLFW_KEY_DELETE),
        _RIGHT(GLFW_KEY_RIGHT),
        _LEFT(GLFW_KEY_LEFT),
        _DOWN(GLFW_KEY_DOWN),
        _UP(GLFW_KEY_UP),
        _PAGE_UP(GLFW_KEY_PAGE_UP),
        _PAGE_DOWN(GLFW_KEY_PAGE_DOWN),
        _HOME(GLFW_KEY_HOME),
        _END(GLFW_KEY_END),
        _CAPS_LOCK(GLFW_KEY_CAPS_LOCK),
        _SCROLL_LOCK(GLFW_KEY_SCROLL_LOCK),
        _NUM_LOCK(GLFW_KEY_NUM_LOCK),
        _PRINT_SCREEN(GLFW_KEY_PRINT_SCREEN),
        _PAUSE(GLFW_KEY_PAUSE),
        _F1(GLFW_KEY_F1),
        _F2(GLFW_KEY_F2),
        _F3(GLFW_KEY_F3),
        _F4(GLFW_KEY_F4),
        _F5(GLFW_KEY_F5),
        _F6(GLFW_KEY_F6),
        _F7(GLFW_KEY_F7),
        _F8(GLFW_KEY_F8),
        _F9(GLFW_KEY_F9),
        _F10(GLFW_KEY_F10),
        _F11(GLFW_KEY_F11),
        _F12(GLFW_KEY_F12),
        _F13(GLFW_KEY_F13),
        _F14(GLFW_KEY_F14),
        _F15(GLFW_KEY_F15),
        _F16(GLFW_KEY_F16),
        _F17(GLFW_KEY_F17),
        _F18(GLFW_KEY_F18),
        _F19(GLFW_KEY_F19),
        _F20(GLFW_KEY_F20),
        _F21(GLFW_KEY_F21),
        _F22(GLFW_KEY_F22),
        _F23(GLFW_KEY_F23),
        _F24(GLFW_KEY_F24),
        _F25(GLFW_KEY_F25),
        _KP_0(GLFW_KEY_KP_0),
        _KP_1(GLFW_KEY_KP_1),
        _KP_2(GLFW_KEY_KP_2),
        _KP_3(GLFW_KEY_KP_3),
        _KP_4(GLFW_KEY_KP_4),
        _KP_5(GLFW_KEY_KP_5),
        _KP_6(GLFW_KEY_KP_6),
        _KP_7(GLFW_KEY_KP_7),
        _KP_8(GLFW_KEY_KP_8),
        _KP_9(GLFW_KEY_KP_9),
        _KP_DECIMAL(GLFW_KEY_KP_DECIMAL),
        _KP_DIVIDE(GLFW_KEY_KP_DIVIDE),
        _KP_MULTIPLY(GLFW_KEY_KP_MULTIPLY),
        _KP_SUBTRACT(GLFW_KEY_KP_SUBTRACT),
        _KP_ADD(GLFW_KEY_KP_ADD),
        _KP_ENTER(GLFW_KEY_KP_ENTER),
        _KP_EQUAL(GLFW_KEY_KP_EQUAL),
        _LEFT_SHIFT(GLFW_KEY_LEFT_SHIFT),
        _LEFT_CONTROL(GLFW_KEY_LEFT_CONTROL),
        _LEFT_ALT(GLFW_KEY_LEFT_ALT),
        _LEFT_SUPER(GLFW_KEY_LEFT_SUPER),
        _RIGHT_SHIFT(GLFW_KEY_RIGHT_SHIFT),
        _RIGHT_CONTROL(GLFW_KEY_RIGHT_CONTROL),
        _RIGHT_ALT(GLFW_KEY_RIGHT_ALT),
        _RIGHT_SUPER(GLFW_KEY_RIGHT_SUPER),
        _MENU(GLFW_KEY_MENU),
        _LAST(GLFW_KEY_LAST);
        
        protected int code;
        private Key(int code) { this.code = code; }
    }
    
}
