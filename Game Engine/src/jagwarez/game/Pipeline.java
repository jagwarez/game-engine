/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

/**
 *
 * @author jacob
 */
public interface Pipeline {
    
    public void init() throws Exception;
    public void load() throws Exception;
    public void render() throws Exception;
    public void destroy() throws Exception;

}
