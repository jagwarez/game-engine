package jagwarez.game;

/**
 *
 * @author jacob
 */
public class Physics implements Pipeline {
    
    private final Program program;
    private final Buffer terrain;
    private final Buffer actors;
    
    public Physics(Game game) {
        program = new Program();
        terrain = game.graphics.terrain.buffer;
        actors = game.graphics.actors.buffer;
    }

    @Override
    public void init() throws Exception {
        program.create();
    }

    @Override
    public void load() throws Exception {
    
    }

    @Override
    public void render() throws Exception {
     
    }

    @Override
    public void destroy() {
        
    }
    
}
