package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Buffer;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Program;

/**
 *
 * @author jacob
 */
public abstract class RenderPipeline extends TexturePipeline implements SharedPipeline {
    
    public final Program program;
    public final Buffer buffer;
    
    public RenderPipeline() {
        
        this.program = new Program();
        this.buffer = new Buffer();
 
        programs.put(getClass(), program);
        buffers.put(getClass(), buffer);
    }
    
    @Override
    public void init(Game game) throws Exception {
        program.create();
        buffer.create();
    }
    
    @Override
    public void destroy() throws Exception {    
        super.destroy();     
        buffer.destroy();     
    }
    
}
