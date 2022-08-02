/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import jagwarez.game.Entity;
import jagwarez.game.Game;
import jagwarez.game.Settings;
import jagwarez.game.Keyboard.Key;
import jagwarez.game.Mouse.Button;
import jagwarez.game.asset.Model;
import jagwarez.game.asset.Terrain;
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
        
        //assets.terrain = new Terrain(1000);
        
        Model model = new ColladaReader(new File(assetsDir, "models/thinmatrix/model.dae")).read();
        assets.models.add(model);
        
        for(int count = 0; count < 1; count++) {
            Entity entity = new Entity(model.name, model);
            entity.position.x = count * 10 * (count % 1 == 0 ? 1 : -1);
            entity.position.z = 10f; //(float)Math.random()*100f;
            //entity.rotation.x = 270; //(float)Math.random()*100f;
            //entity.rotation.y = 0;
            //entity.animation("Alien-Animal_1_5_Baked");
            world.entities.add(entity);
        }
        
        //world.camera.position.z = -10f;
        world.camera.position.y = 5f;
        world.camera.rotation.y = 180;
    }
    
    @Override
    public void loop() {
        
        if(keyboard.pressed(Key._ESCAPE)) {
            window.close();
        }

        float fx = 0f, fy = 0f, fz = 0f;

        if(keyboard.pressed(Key._W)) {
            fz += .2f;
        }

        if(keyboard.pressed(Key._S))
            fz += -.2f;

        if(keyboard.pressed(Key._A))
            fx += .2f;

        if(keyboard.pressed(Key._D))
            fx += -.2f;

        if(keyboard.pressed(Key._SPACE))
            fy += .2f;

        if(mouse.pressed(Button.RIGHT)) {
            world.camera.rotation.x += mouse.y() >= window.height()/2 ? .2f : -.2f;
            world.camera.rotation.y += mouse.x() >= window.width()/2 ? .2f : -.2f;
        }
        
        world.camera.position.x += fx; //mouse.x() >= window.width()/2 ? .3f : -.3f;
        world.camera.position.z += fz;

        //for(Entity entity : world.entities)
            //entity.rotation.y++;
       
    }
    
    public static void main(String[] args) throws Exception {
        try(Game game = new TestGame()) {
            game.play();
        }
    }
}
