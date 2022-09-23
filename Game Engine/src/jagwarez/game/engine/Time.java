package jagwarez.game.engine;

/**
 *
 * @author jacob
 */
public class Time {
    
    private static long current = System.currentTimeMillis();
    private static long previous = 0l;
    private static long elapsed = 0l;
    
    public static void update() {
        previous = current;
        current = System.currentTimeMillis();
        elapsed = current-previous;
    }
    
    public static long current() { return current; }
    public static long previous() { return previous; }
    public static long elapsed() { return elapsed; }
    
}
