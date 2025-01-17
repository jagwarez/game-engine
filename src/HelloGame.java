

import jagwarez.game.asset.model.Texture;
import jagwarez.game.engine.Actor;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Keyboard.Key;
import jagwarez.game.engine.Light;
import jagwarez.game.engine.Settings;
import java.io.File;

/**
 *
 * @author jacob
 */
public class HelloGame extends Game { 
    
    public HelloGame() {
        super(new Settings().title("Test Game").size(1600, 900));
    }
    
    @Override
    public void load() throws Exception {
        
        world.camera.position.x = 500f;
        world.camera.position.y = 200f;
        
        File assetsDir = new File("games/hello/assets");
        
        world.sky.model = assets.models.load(new File(assetsDir, "models/skydome/skydome.obj"));
        world.lights.add(new Light().radius(100f).position(512f, 180f, 0).color(.5f, 0f, 1f));
        world.lights.add(new Light().position(512f, 180f, 1024f).color(1f, 0f, 0f));
        
        world.terrain.heightmap = new Texture(new File(assetsDir, "terrain/terrain.png"));
        
        for(String actor : new String[] {"nordstrom", "mawlaygo"})
            assets.models.load(new File(assetsDir, "models/"+actor+"/"+actor+".dae"));
        
        world.player.model = assets.models.get("nordstrom");
        world.player.scale.set(.08f);
        world.player.position.x = 525f;
        world.player.position.z = 1f;
        ////world.player.rotation.y = 180;
        
        Actor boss = new Actor("boss", assets.models.get("mawlaygo"));
        boss.position.x = 500f;
        boss.position.z = 700f;
        boss.rotation.y = 160f;
        boss.scale.set(.6f);
        boss.speed = .5f;
        boss.animation("walk");
        world.actors.add(boss);
        
        keyboard.binds.put(Key._ESCAPE, (key) -> {
            if(key.pressed()) exit();
        });
        
        keyboard.binds.put(Key._TAB, (key) -> {
            if(key.released()) graphics.wireframe();
        });
        
        keyboard.binds.put(Key._F, (key) -> {
            if(key.released()) graphics.fog = !graphics.fog;
        });
        
        keyboard.binds.put(Key._W, (key) -> {
            switch(key.state) {
                case PRESSED:
                    if(world.camera.target.id != world.camera.id)
                        world.camera.target.animation("run");
                case DOWN:
                    world.camera.target.forward();
                    break;
                case RELEASED:
                    if(world.camera.target.id != world.camera.id)
                        world.camera.target.animation("idle1");
            }
        });
        
        keyboard.binds.put(Key._S, (key) -> {
            switch(key.state) {
                case PRESSED:
                    if(world.camera.target.id != world.camera.id)
                        world.camera.target.animation("back");
                case DOWN:
                    world.camera.target.backward();
                    break;
                case RELEASED:
                    if(world.camera.target.id != world.camera.id)
                        world.camera.target.animation("idle1");
            }
        });
        
        keyboard.binds.put(Key._A, (key) -> {
            if(key.down()) world.camera.target.left();
        });
        
        keyboard.binds.put(Key._D, (key) -> {
            if(key.down()) world.camera.target.right();
        });
        
        keyboard.binds.put(Key._UP, (key) -> {
            if(key.down()) world.camera.rotation.x += .5f;
        });
        
        keyboard.binds.put(Key._DOWN, (key) -> {
            if(key.down()) world.camera.rotation.x -= .5f;
        });
        
        keyboard.binds.put(Key._C, (key) -> {
            if(key.down()) world.camera.target.position.y -= 1f;
        });
        
        keyboard.binds.put(Key._SPACE, (key) -> {
            if(key.down()) world.camera.target.position.y += 2f;
        });
        
        keyboard.binds.put(Key._KP_7, (key) -> {
            if(key.down())
                world.lights.get(0).position.z += 5f;
        });
        
        keyboard.binds.put(Key._KP_1, (key) -> {
            if(key.down())
                world.lights.get(0).position.z -= 5f;
        });
        
        keyboard.binds.put(Key._KP_8, (key) -> {
            if(key.down())
                for(Light light : world.lights)
                    light.intensity+=.01f;
        });
        
        keyboard.binds.put(Key._KP_2, (key) -> {
            if(key.down())
                for(Light light : world.lights)
                    light.intensity = (float) Math.max(light.intensity-.01f, 0);
        });
        
        keyboard.binds.put(Key._KP_9, (key) -> {
            if(key.down())
                world.lights.get(1).position.z += 5f;
        });
        
        keyboard.binds.put(Key._KP_3, (key) -> {
            if(key.down())
                world.lights.get(1).position.z -= 5f;
        });
        
        keyboard.binds.put(Key._KP_ADD, (key) -> {
            if(key.pressed()) world.camera.target = world.player;
        });
        
        keyboard.binds.put(Key._KP_SUBTRACT, (key) -> {
            if(key.pressed()) world.camera.target = world.camera;
        });
        
        keyboard.binds.put(Key._LEFT, (key) -> {
            if(key.down()) world.camera.target.rotation.y += .8f;
        });
        
        keyboard.binds.put(Key._RIGHT, (key) -> {
            if(key.down()) world.camera.target.rotation.y -= .8f;
        });
        
    }
    
    @Override
    public void update() {
        world.actors.get(0).rotation.y += .2f;
        world.actors.get(0).forward();
    }
    
    public static void main(String[] args) throws Exception {
        try(Game game = new HelloGame()) {
            game.play();
        }
    }
}
