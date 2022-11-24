package jagwarez.game.asset.model.reader;

import jagwarez.game.asset.AssetReader;
import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.reader.dae.DAEModelReader;
import jagwarez.game.asset.model.reader.obj.OBJModelReader;
import java.io.File;

/**
 *
 * @author jacob
 */
public class ModelReader implements AssetReader<Model> {

    @Override
    public Model read(File file) throws Exception {
        
        if(file.isFile()) {
            
            if(file.getName().toLowerCase().endsWith(".obj"))
                return new OBJModelReader().read(file);
            else if(file.getName().toLowerCase().endsWith(".dae"))
                return new DAEModelReader().read(file);
            
        }
        
        throw new IllegalArgumentException("Unrecognized model file format.");
        
    }

}
