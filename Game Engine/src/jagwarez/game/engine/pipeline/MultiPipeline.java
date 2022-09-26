package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Game;
import jagwarez.game.engine.Pipeline;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jacob
 */
public abstract class MultiPipeline implements Pipeline {
    
    protected final List<Pipeline> pipelines = new ArrayList<>();
    
    @Override
    public void init(Game game) throws Exception {
        for(Pipeline pipeline : pipelines)
            pipeline.init(game);
    }

    @Override
    public void load() throws Exception {
        for(Pipeline pipeline : pipelines)
            pipeline.load();
    }

    @Override
    public void process() throws Exception {
        for(Pipeline pipeline : pipelines)
            pipeline.process();
    }

    @Override
    public void destroy() throws Exception {
        for(Pipeline pipeline : pipelines)
            pipeline.destroy();
    }

}
