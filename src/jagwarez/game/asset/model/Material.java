package jagwarez.game.asset.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jacob
 */
public class Material {
    
    public final Map<Effect.Parameter,Effect> effects;
    
    public Material() {
        this.effects = new HashMap<>();
    }
}
