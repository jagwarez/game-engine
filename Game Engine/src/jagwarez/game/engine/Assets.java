package jagwarez.game.engine;

import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.reader.ModelReader;
import jagwarez.game.asset.sound.Sound;
import jagwarez.game.asset.sound.reader.SoundReader;
import java.io.File;
import java.util.HashMap;

/**
 *
 * @author jacob
 */
public class Assets {
    
    public final Models models = new Models();
    public final Sounds sounds = new Sounds();
    
    public static class Models extends HashMap<String,Model> {
        private final ModelReader reader = new ModelReader();
        public Model load(File file) throws Exception {
            Model model = reader.read(file);
            put(model.name, model);
            return model;
        }
    }
    
    public static class Sounds extends HashMap<String,Sound> {
        private final SoundReader reader = new SoundReader();
        public Sound load(File file) throws Exception {
            Sound sound = reader.read(file);
            put(sound.name, sound);
            return sound;
        }
    }
     
}


