package jagwarez.game;

/**
 *
 * @author jacob
 */
public interface Pipeline {
    
    public void init() throws Exception;
    public void load() throws Exception;
    public void render() throws Exception;
    public void destroy() throws Exception;

}
