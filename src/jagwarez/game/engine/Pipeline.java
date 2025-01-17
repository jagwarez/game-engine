package jagwarez.game.engine;

/**
 *
 * @author jacob
 */
public interface Pipeline {
    
    public void init(Game game) throws Exception;
    public void load() throws Exception;
    public void execute() throws Exception;
    public void destroy() throws Exception;

}
