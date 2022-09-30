import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.Texture;
import jagwarez.game.asset.model.reader.ColladaReader;
import jagwarez.game.asset.model.reader.WavefrontReader;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Keyboard.Key;
import jagwarez.game.engine.Light;
import jagwarez.game.engine.Mouse.Button;
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
        world.player.position.y = 100;
        //world.player.rotation.y = 180;
        
        //world.camera.rotation.y = 180;
        world.camera.follow(world.player);
    }
    
    @Override
    public void update() {
        
        if(keyboard.pressed(Key._ESCAPE)) {
            window.close();
        }
        
        if(keyboard.pressed(Key._TAB))
            graphics.wireframe();

        float fx = 0f, fy = 0f, fz = 0f;

        if(keyboard.pressed(Key._W)) {
            world.player.animation("Armature");
            world.player.forward();
        } else {
            world.player.animation(null);
        }

        if(keyboard.pressed(Key._S))
            world.player.backward();

        if(keyboard.pressed(Key._A))
            world.player.rotation.y += 1f;

        if(keyboard.pressed(Key._D))
            world.player.rotation.y -= 1f;

        if(keyboard.pressed(Key._SPACE))
            fy += .2f;
        
        if(keyboard.pressed(Key._V))
            fy -= .2f;
        
        if(keyboard.pressed(Key._UP))
            world.lights.get(0).position.z += 1f;
        
        if(keyboard.pressed(Key._DOWN)) {
            world.lights.get(0).position.z -= 1f;
            //world.camera.tether.distance += .02f;
        }

        if(mouse.pressed(Button.RIGHT)) {
            world.camera.rotation.x += mouse.y() >= window.height()/2 ? .5f : -.5f;
            world.camera.rotation.y += mouse.x() >= window.width()/2 ? .5f : -.5f;
        }
        
        world.player.position.x += fx; //mouse.x() >= window.width()/2 ? .3f : -.3f;
        world.player.position.y += fy;
        world.player.position.z += fz;
        
        
        //world.lights.get(0).position.x %= world.terrain.heightmap.width;
        //world.lights.get(0).position.z += 1f;
        
        //System.out.println("Player: x="+world.player.position.x+", z="+world.player.position.z);
            
    }
    
    public static void main(String[] args) throws Exception {
        try(Game game = new TestGame()) {
            game.play();
        }
    }
}
