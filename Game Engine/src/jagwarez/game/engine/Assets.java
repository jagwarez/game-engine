package jagwarez.game.engine;

import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.reader.ModelReader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jake
 */
public class Assets {
    
    public final Reader read = new Reader();
    
    public final Map<String,Model> models = new HashMap<>();
    
    public class Reader {
        
        private final ModelReader modelReader = new ModelReader();;
        
        private Reader() { }
 
        public Model model(File file) throws Exception {
            Model model = modelReader.read(file);
            models.put(model.name, model);
            return model;
        }
    }
    
}


