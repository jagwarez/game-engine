package jagwarez.game.engine.pipeline;

/**
 *
 * @author jacob
 */
public class GamePipeline extends MultiPipeline {
    
    public GamePipeline() {
        pipelines.add(new PhysicsPipeline());
        pipelines.add(new SelectPipeline());
        pipelines.add(new GraphicsPipeline());
    }
    
}
