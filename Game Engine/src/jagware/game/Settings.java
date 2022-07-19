/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game;

/**
 *
 * @author jacob
 */
public class Settings {
    
    private String title = "Game";
    private int width = 800;
    private int height = 600;
    private boolean resize = true;
    private boolean visible = true;
    
    public Settings title(String title) {
        this.title = title;
        return this;
    }
    
    public String title() {
        return title;
    }
    
    public Settings width(int width) {
        this.width = width;
        return this;
    }
    
    public int width() {
        return width;
    }
    
    public Settings height(int height) {
        this.height = height;
        return this;
    }
    
    public int height() {
        return height;
    }
    
    public Settings size(int width, int height) {
        this.width = width; this.height = height;
        return this;
    }
    
    public Settings resize(boolean resize) {
        this.resize = resize;
        return this;
    }
    
    public boolean resize() {
        return resize;
    }
    
    public Settings visible(boolean visible) {
        this.visible = visible;
        return this;
    }
    
    public boolean visible() {
        return visible;
    }
    
}
