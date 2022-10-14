package jagwarez.game.engine;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author jacob
 */
public class Script extends Identity {
    
    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    
    private final Actor actor;
    private Invocable function = null;
    
    public Script(Actor actor) {
        this.actor = actor;
    }
    
    public void init() throws Exception {
        
    }
    
    public void invoke() throws Exception {
        if(function != null)
            function.invokeFunction("actor_"+id, actor);
    }
}
