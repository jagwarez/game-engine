

import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.Texture;
import jagwarez.game.asset.model.reader.dae.DAEModelReader;
import jagwarez.game.asset.model.reader.obj.OBJModelReader;
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
public class TestGame extends Game { 
    
    public TestGame() {
        super(new Settings().title("Test Game").size(1600, 1200));
    }
    
    @Override
    public void load() throws Exception {
        
        world.camera.position.x = 512f;
        
        File assetsDir = new File("games/hello/assets");
        
        world.sky.model = new OBJModelReader().read(new File(assetsDir, "models/skydome/skydome.obj"));
        world.lights.add(new Light());
        world.lights.add(new Light());
        
        world.lights.get(0).color.rgb(1f, 0f, .1f);
        world.lights.get(0).position.x = 700;
        world.lights.get(0).position.y = 200;
        world.lights.get(0).position.z = 0;
        world.lights.get(0).intensity = .5f;
        
        world.lights.get(1).color.rgb(.1f, 0f, 1f);
        world.lights.get(1).position.x = 300;
        world.lights.get(1).position.y = 200;
        world.lights.get(1).position.z = 0;
        
        world.terrain.heightmap = new Texture(new File(assetsDir, "terrain/arena-1.png"));
        
        DAEModelReader actorReader = new DAEModelReader();
        Model model = actorReader.read(new File(assetsDir, "models/mawlaygo/mawlaygo.dae"));
        assets.models.add(model);
        
        //new DAEAnimationReader(new File(assetsDir, "models/mawlaygo/
        
        world.player.model = model;
        //world.player.scale.set(.08f);
        world.player.position.x = 512f;
        world.player.position.z = 700f;
        //world.camera.position.y = 80;
        world.player.rotation.y = 180;
        //world.player.animation("idle1");
        
        Actor cowboy = new Actor("cowboy", model);
        cowboy.position.x = 100f;
        cowboy.position.z = 100f;
        //cowboy.scale.set(.001f);
        cowboy.speed = .1f;
        cowboy.animation("Armature");
        //world.actors.add(cowboy);
        
        keyboard.binds.put(Key._ESCAPE, (key) -> {
            if(key.pressed()) stop();
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
                        world.camera.target.animation("Armature");
                case DOWN:
                    world.camera.target.forward();
                    break;
                case RELEASED:
                    if(world.camera.target.id != world.camera.id)
                        world.camera.target.animation("idle1");
            }
        });
        
        keyboard.binds.put(Key._S, (key) -> {
            if(key.down()) world.camera.target.backward();
        });
        
        keyboard.binds.put(Key._A, (key) -> {
            if(key.down()) world.camera.target.left();
        });
        
        keyboard.binds.put(Key._D, (key) -> {
            if(key.down()) world.camera.target.right();
        });
        
        keyboard.binds.put(Key._UP, (key) -> {
            if(key.down() && world.camera.target.id == world.camera.id) world.camera.rotation.x += .5f;
        });
        
        keyboard.binds.put(Key._DOWN, (key) -> {
            if(key.down() && world.camera.target.id == world.camera.id) world.camera.rotation.x -= .5f;
        });
        
        keyboard.binds.put(Key._C, (key) -> {
            if(key.down()) world.camera.target.position.y -= 1f;
        });
        
        keyboard.binds.put(Key._SPACE, (key) -> {
            if(key.down()) world.camera.target.position.y += 4f;
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
                    light.position.y += 5f;
        });
        
        keyboard.binds.put(Key._KP_2, (key) -> {
            if(key.down())
                for(Light light : world.lights)
                    light.position.y -= 5f;
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
        //world.actors.get(0).forward();
    }
    
    public static void main(String[] args) throws Exception {
        try(TestGame test = new TestGame()) {
            test.play();
        }
    }
}
