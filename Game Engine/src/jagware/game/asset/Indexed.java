/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

/**
 *
 * @author jacob
 */
public class Indexed {
    
    public int index;
    
    public Indexed(int index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        return this.index;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null ? this.getClass().equals(obj.getClass()) && this.index == ((Indexed)obj).index : false;
    }
}
