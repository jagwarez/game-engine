package jagwarez.game.engine;

/**
 *
 * @author jacob
 */
public class Time {
    
    private static long millis = System.currentTimeMillis();
    private static long previous = 0l;
    private static long elapsed = 0l;
    
    public static void update() {
        previous = millis;
        millis = System.currentTimeMillis();
        elapsed = millis-previous;
    }
    
    public static long millis() { return millis; }
    public static long seconds() { return millis/1000l; }
    public static long previous() { return previous; }
    public static long elapsed() { return elapsed; }
    
}
