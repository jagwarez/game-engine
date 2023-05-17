package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Game;
import jagwarez.game.engine.Mouse;
import jagwarez.game.engine.Program;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Terrain;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;
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
    
    private Mouse mouse;
    private Terrain terrain;
    
    @Override
    public void init(Game game) throws Exception {
        
        super.init(game);
        
        mouse = game.mouse;
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
                    
                    indexBuffer.put(x * Terrain.SIZE + (z-1));
                    indexBuffer.put((x-1) * Terrain.SIZE + (z-1));
                    indexBuffer.put((x-1) * Terrain.SIZE + z);
                    
                    indexBuffer.put(x * Terrain.SIZE + z);
                    indexBuffer.put(x * Terrain.SIZE + (z-1));
                    indexBuffer.put((x-1) * Terrain.SIZE + z);
                    
                }
            }
        }

        program.attach(new Shader("jagwarez/game/engine/pipeline/program/terrain/vs.glsl", Shader.Type.VERTEX));
        program.attach(new Shader("jagwarez/game/engine/pipeline/program/terrain/fs.glsl", Shader.Type.FRAGMENT));
        program.attribute(0, "position");
        program.fragment(0, "color");
        program.link();
        
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
    public void execute() throws Exception {
        
        program.enable();
        buffer.bind();
        
        sky();
        
        fog();
        
        lights();
        
        if(mouse.target.id == terrain.id) {
            program.uniform("target").bool(true);
            program.uniform("target_position").vector(mouse.target.position);
        } else
            program.uniform("target").bool(false);
        
        render();
        
        buffer.unbind();
        program.disable();   
    }
    
    @Override
    public void render(Program program) throws Exception {
        
        program.uniform("world").matrix(world);
        program.uniform("camera").matrix(camera);
        program.uniform("transform").matrix(terrain);
        program.uniform("identity").integer(terrain.id);
        program.uniform("map_width").integer(terrain.heightmap.width);
        program.uniform("map_length").integer(terrain.heightmap.height);
            
        glActiveTexture(GL_TEXTURE0 + 0);
        glBindTexture(GL_TEXTURE_2D, terrain.heightmap.id);
        program.uniform("height_map").integer(0);

        glDrawElements(GL_TRIANGLES, Terrain.INDEX_COUNT, GL_UNSIGNED_INT, 0);
        
        glBindTexture(GL_TEXTURE_2D, 0);
        
    }
}
