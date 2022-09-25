package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Game;
import jagwarez.game.engine.Pipeline;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jacob
 */
public class GamePipeline implements Pipeline {
    
    private final List<Pipeline> pipelines = new ArrayList<>();
    
    public GamePipeline() {
        //pipelines.add(new PhysicsPipeline());
        pipelines.add(new GraphicsPipeline());
    }
    
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
