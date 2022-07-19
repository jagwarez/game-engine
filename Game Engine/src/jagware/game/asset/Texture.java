/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

import java.io.File;

/**
 *
 * @author jacob
 */
public class Texture extends Effect {
    
    public int id = -1;
    public final File file;
    
    public Texture(File file) {
        super(Type.TEXTURE);
        this.file = file;
    }
}
