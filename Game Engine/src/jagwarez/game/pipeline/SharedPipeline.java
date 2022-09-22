package jagwarez.game.pipeline;

import jagwarez.game.engine.Buffer;
import jagwarez.game.engine.Pipeline;
import jagwarez.game.engine.Program;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jacob
 */
public interface SharedPipeline extends Pipeline {
    public static final Map<Class<? extends Pipeline>,Program> programs =  new HashMap<>();
    public static final Map<Class<? extends Pipeline>,Buffer> buffers = new HashMap<>();
}
