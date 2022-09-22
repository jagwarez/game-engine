package jagwarez.game.asset;

/**
 *
 * @author jacob
 */
public interface AssetReader<O> {
    public O read() throws Exception;
}
