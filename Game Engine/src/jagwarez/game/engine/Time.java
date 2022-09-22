package jagwarez.game.engine;

/**
 *
 * @author jacob
 */
public class Time {
    
    private float current = 0f;
    private float previous = 0f;
    private float elapsed = 0f;
    
    protected void tick() {
        previous = current;
        current = System.currentTimeMillis()/1000f;
        elapsed = current-previous;
    }
    
    public float current() { return current; }
    public float previous() { return previous; }
    public float elapsed() { return elapsed; }
    
}
