package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Buffer;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Program;

/**
 *
 * @author jacob
 */
abstract class RenderPipeline extends TexturePipeline implements SharedPipeline {
    
    protected final Program program;
    protected final Buffer buffer;
    
    public RenderPipeline() {
        
        this.program = new Program();
        this.buffer = new Buffer();
 
        programs.put(getClass(), program);
        buffers.put(getClass(), buffer);
    }
    
    @Override
    public void init(Game game) throws Exception {
        program.init();
        buffer.init();
    }
    
    @Override
    public void destroy() throws Exception {    
        super.destroy();     
        buffer.destroy();     
    }
    
}
