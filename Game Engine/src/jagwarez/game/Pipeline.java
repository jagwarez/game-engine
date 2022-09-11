/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

/**
 *
 * @author jacob
 */
public interface Pipeline<A> {
    
    public Pipeline load() throws Exception;
    public void render(A asset) throws Exception;
    public void destroy();

}
