/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.asset;

/**
 *
 * @author jacob
 */
public abstract class Effect {
    
    public final Type type;
    
    public Effect(Type type) {
        this.type = type;
    }
    
    public static enum Parameter {
        DIFFUSE,
        SPECULAR;
        
        public static Parameter fromString(String p) {
            switch(p.toLowerCase()) {
                case "diffuse": return DIFFUSE;
                case "specular": return SPECULAR;
                default: return null;
            }
        }
    }
    
    public static enum Type {
        COLOR,
        TEXTURE
    }
}
