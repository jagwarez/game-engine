

import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.Texture;
import jagwarez.game.asset.model.reader.ColladaReader;
import jagwarez.game.asset.model.reader.WavefrontReader;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Keyboard.Key;
import jagwarez.game.engine.Light;
import jagwarez.game.engine.Settings;
import java.io.File;

/**
 *
 * @author jacob
 */
public class TestGame extends Game {
    
    public TestGame() {
        super(new Settings().title("Test Game").size(1600, 1200));
    }
    
    @Override
    public void load() throws Exception {
        
        File assetsDir = new File("games/hello/assets");
        
        world.sky.model = new WavefrontReader(new File(assetsDir, "models/skydome/skydome.obj")).read();
        world.lights.add(new Light());
        
        world.lights.get(0).position.y = 200;
        //world.lights.get(0).position.z = 300;
        
        world.terrain.heightmap = new Texture(new File(assetsDir, "terrain/terrain.png"));
        
        Model model = new ColladaReader(new File(assetsDir, "models/thinmatrix/model.dae")).read();
        assets.models.add(model);
        
        world.player.model = model;
        world.player.scale.set(.01f);
        //world.player.position.x = world.terrain.OFFSET;
        //world.player.position.z = world.terrain.OFFSET;
        world.player.position.y = 80;
        //world.player.rotation.y = 180;
        
        //world.camera.rotation.y = 180;
        world.camera.follow(world.player);
        
        keyboard.binds.put(Key._ESCAPE, (key) -> {
            if(key.pressed()) window.close();
        });
        
        keyboard.binds.put(Key._TAB, (key) -> {
            if(key.pressed()) graphics.wireframe();
        });
        
        keyboard.binds.put(Key._W, (key) -> {
            switch(key.state) {
                case PRESSED:
                    world.player.animation("Armature");
                case DOWN:
                    world.player.forward();
                    break;
                case RELEASED:
                    world.player.animation(null);
            }
        });
        
        keyboard.binds.put(Key._S, (key) -> {
            if(key.down()) world.player.backward();
        });
        
        keyboard.binds.put(Key._A, (key) -> {
            if(key.down()) world.player.left();
        });
        
        keyboard.binds.put(Key._D, (key) -> {
            if(key.down()) world.player.right();
        });
        
        keyboard.binds.put(Key._UP, (key) -> {
            if(key.down()) world.lights.get(0).position.z += .5f;
        });
        
        keyboard.binds.put(Key._DOWN, (key) -> {
            if(key.down()) world.lights.get(0).position.z -= .5f;
        });
    }
    
    @Override
    public void update() {
        
          
    }
    
    public static void main(String[] args) throws Exception {
        try(TestGame test = new TestGame()) {
            test.play();
        }
    }
}
