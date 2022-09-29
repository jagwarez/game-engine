package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Game;
import jagwarez.game.engine.Player;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Terrain;
import jagwarez.game.engine.World;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 *
 * @author jacob
 */
public class TerrainPipeline extends RenderPipeline {
    
    private World world;
    private Player player;
    private Terrain terrain;
    
    @Override
    public void init(Game game) throws Exception {
        
        super.init(game);
        
        world = game.world;
        player = world.player;
        terrain = world.terrain;
    }
    
    @Override
    public void load() throws Exception {
        
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(Terrain.INDEX_COUNT);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(Terrain.VERTEX_COUNT);
             
        for(int x = 0; x < Terrain.SIZE; x++) {
            for(int z = 0; z < Terrain.SIZE; z++) {
                
                vertexBuffer.put(x-Terrain.OFFSET);
                vertexBuffer.put(z-Terrain.OFFSET);

                if(x > 0 && z > 0) {
                    
                    indexBuffer.put(x * Terrain.SIZE + z);
                    indexBuffer.put(x * Terrain.SIZE + (z-1));
                    indexBuffer.put((x-1) * Terrain.SIZE + (z-1));
                    
                    indexBuffer.put((x-1) * Terrain.SIZE + (z-1));
                    indexBuffer.put((x-1) * Terrain.SIZE + z);
                    indexBuffer.put(x * Terrain.SIZE + z);
                    
                }
            }
        }

        program.bindShader(new Shader("jagwarez/game/engine/pipeline/program/terrain/vs.glsl", Shader.Type.VERTEX));
        program.bindShader(new Shader("jagwarez/game/engine/pipeline/program/terrain/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindFragment(0, "color");
        
        buffer.bind();
        
        buffer.elements((IntBuffer) indexBuffer.flip());
        buffer.attribute((FloatBuffer)vertexBuffer.flip(), 2);
        
        buffer.unbind();
        
        Map<Integer,Integer> parameters = terrain.heightmap.parameters;
        parameters.put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        parameters.put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        parameters.put(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        parameters.put(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        
        texture(terrain.heightmap);
    }

    @Override
    public void process() throws Exception {
        
        program.enable();
        buffer.bind();
        
        //lights();
        
        float x = -(player.position.x % 1f);
        float z = -(player.position.z % 1f);

        terrain.identity();
        terrain.translate(x, 0f, z);
        
        program.bindUniform("view").setMatrix4f(world);
        program.bindUniform("model").setMatrix4f(terrain);
        program.bindUniform("sky_color").set3f(world.sky.color.r, world.sky.color.g, world.sky.color.b);
        program.bindUniform("map_color").set3f(.6f, 0f, 0f);

        if(terrain.heightmap != null) {
            
            Vector2f offset = new Vector2f((float)Math.floor(player.position.x), 
                                           (float)Math.floor(player.position.z));
            
            glActiveTexture(GL_TEXTURE0 + 0);
            glBindTexture(GL_TEXTURE_2D, terrain.heightmap.id);

            program.bindUniform("hscale").set1f(terrain.SCALE);
            program.bindUniform("twidth").set1f(terrain.heightmap.width-1);
            program.bindUniform("offset").set2f(offset.x, offset.y);
            program.bindUniform("use_hmap").setBool(true);
            program.bindUniform("hmap").set1i(0);

        } else {
            program.bindUniform("use_hmap").setBool(false);
        }

        glDrawElements(GL_TRIANGLES, Terrain.INDEX_COUNT, GL_UNSIGNED_INT, 0);
        
        glBindTexture(GL_TEXTURE_2D, 0);
        
        buffer.unbind();
        program.disable();     
    }
    
}
