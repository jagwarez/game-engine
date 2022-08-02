/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagwarez.game.asset;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jake
 */
public class Material {
    
    public final Map<Effect.Parameter,Effect> effects;
    
    public Material() {
        this.effects = new HashMap<>();
    }
}
