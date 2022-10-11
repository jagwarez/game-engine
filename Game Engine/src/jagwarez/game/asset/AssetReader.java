package jagwarez.game.asset;

import java.io.File;

/**
 *
 * @author jacob
 */
public interface AssetReader<O> {
    public O read(File file) throws Exception;
}
