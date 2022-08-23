/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

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
