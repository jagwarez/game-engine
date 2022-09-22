package jagwarez.game.engine;

/**
 *
 * @author jacob
 */
public interface Pipeline {
    
    public void init(Game game) throws Exception;
    public void load() throws Exception;
    public void render() throws Exception;
    public void destroy() throws Exception;

}
