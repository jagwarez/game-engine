package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Pipeline;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jacob
 */
public interface SharedPipeline extends Pipeline {
    public static final Map<Class<? extends Pipeline>,Pipeline> pipelines = new HashMap<>();
}
