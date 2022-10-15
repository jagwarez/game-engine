package jagwarez.game.asset.model.reader.dae;

import jagwarez.game.asset.AssetReader;
import jagwarez.game.asset.model.Model;
import java.io.File;

/**
 *
 * @author jacob
 */
public class DAEFolderReader implements AssetReader<Model> {
    
    private final DAEModelReader modelReader = new DAEModelReader();

    @Override
    public Model read(File file) throws Exception {
        
        Model model = modelReader.read(file);

        File animDir = new File(file.getParentFile(), "animations");

        File[] animFiles = animDir.listFiles((File dir, String name) -> name.endsWith(".dae"));
        
        if(animFiles != null) {
            
            DAEAnimationReader animReader = new DAEAnimationReader(model);

            for(File animFile : animFiles)           
                animReader.read(animFile);
        }
        
        return model;
       
    }

}
