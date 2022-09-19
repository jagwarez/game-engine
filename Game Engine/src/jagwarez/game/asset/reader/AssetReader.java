package jagwarez.game.asset.reader;

/**
 *
 * @author jacob
 */
public interface AssetReader<O> {
    public O read() throws Exception;
}
