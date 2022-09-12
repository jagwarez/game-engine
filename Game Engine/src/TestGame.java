/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import jagwarez.game.Game;
import jagwarez.game.Keyboard.Key;
import jagwarez.game.Mouse.Button;
import jagwarez.game.Settings;
import jagwarez.game.Sky;
import jagwarez.game.Terrain;
import jagwarez.game.asset.Model;
import jagwarez.game.asset.Texture;
import jagwarez.game.asset.reader.ColladaReader;
import java.io.File;

/**
 *
 * @author Jake
 */
public class TestGame extends Game {
    
    public TestGame() {
        super(new Settings().title("Test Game").size(1600, 1200));
    }
    
    @Override
    public void load() throws Exception {
        System.out.println("Loading game... ");
        
        File assetsDir = new File("games/hello/assets");
        
        world.sky.textures[Sky.RIGHT] = new Texture(new File(assetsDir, "textures/skybox/right.png"));
        world.sky.textures[Sky.LEFT] = new Texture(new File(assetsDir, "textures/skybox/left.png"));
        world.sky.textures[Sky.TOP] = new Texture(new File(assetsDir, "textures/skybox/top.png"));
        world.sky.textures[Sky.BOTTOM] = new Texture(new File(assetsDir, "textures/skybox/bottom.png"));
        world.sky.textures[Sky.FRONT] = new Texture(new File(assetsDir, "textures/skybox/front.png"));
        world.sky.textures[Sky.BACK] = new Texture(new File(assetsDir, "textures/skybox/back.png"));
        
        world.terrain.grid[0][0].heightmap = new Texture(new File(assetsDir, "terrain/heightmap1.png"));
        world.terrain.grid[0][1].heightmap = new Texture(new File(assetsDir, "terrain/heightmap.png"));
        
        Model model = new ColladaReader(new File(assetsDir, "models/thinmatrix/model.dae")).read();
        assets.models.add(model);
        
        world.player.model = model;
        world.player.position.x = Terrain.Patch.WIDTH/2;
        world.player.position.z = Terrain.Patch.WIDTH/2;
        world.player.rotation.y = 180;
        
        System.out.println("width="+world.terrain.width);
        
        world.camera.rotation.y = 180;
        world.camera.follow(world.player);
    }
    
    @Override
    public void loop() {
        
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
            world.player.left();

        if(keyboard.pressed(Key._D))
            world.player.right();

        if(keyboard.pressed(Key._SPACE))
            fy += .2f;
        
        if(keyboard.pressed(Key._V))
            fy -= .2f;
        
        if(keyboard.pressed(Key._UP))
            world.camera.tether.distance -= .1f;
        
        if(keyboard.pressed(Key._DOWN))
            world.camera.tether.distance += .1f;

        if(mouse.pressed(Button.RIGHT)) {
            //world.player.rotation.x += mouse.y() >= window.height()/2 ? 1f : -1f;
            world.player.rotation.y += mouse.x() >= window.width()/2 ? -1f : 1f;
        }
        
        world.player.position.x += fx; //mouse.x() >= window.width()/2 ? .3f : -.3f;
        world.player.position.y += fy;
        world.player.position.z += fz;
       
    }
    
    public static void main(String[] args) throws Exception {
        try(Game game = new TestGame()) {
            game.play();
        }
    }
}
