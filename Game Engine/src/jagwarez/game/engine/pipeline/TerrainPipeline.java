package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Actor;
import jagwarez.game.engine.Camera;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Sky;
import jagwarez.game.engine.Terrain;
import jagwarez.game.engine.World;
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
    
    private World world;
    private Sky sky;
    private Camera camera;
    private Terrain terrain;
    
    @Override
    public void init(Game game) throws Exception {
        
        super.init(game);
        
        world = game.world;
        sky = world.sky;
        camera = world.camera;
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

        program.attach(new Shader("jagwarez/game/engine/pipeline/program/terrain/vs.glsl", Shader.Type.VERTEX));
        program.attach(new Shader("jagwarez/game/engine/pipeline/program/terrain/fs.glsl", Shader.Type.FRAGMENT));
        program.attribute(0, "position");
        program.fragment(0, "color");
        
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
        
        lights();
        
        Actor target = camera.target != null ? camera.target : camera;
        
        terrain.identity();
        terrain.translate(quantize(target.position.x), 0f, quantize(target.position.z));
        
        program.uniform("world").mat4f(world);
        program.uniform("camera").mat4f(world.camera);
        program.uniform("terrain").mat4f(terrain);
        program.uniform("sky_color").float3(sky.color.r, sky.color.g, sky.color.b);
        program.uniform("hscale").float1(terrain.SCALE);
        program.uniform("twidth").float1(terrain.heightmap.width-1);
        program.uniform("theight").float1(terrain.heightmap.height-1);
        
        glActiveTexture(GL_TEXTURE0 + 0);
        glBindTexture(GL_TEXTURE_2D, terrain.heightmap.id);
        program.uniform("hmap").int1(0);

        glDrawElements(GL_TRIANGLES, Terrain.INDEX_COUNT, GL_UNSIGNED_INT, 0);
        
        glBindTexture(GL_TEXTURE_2D, 0);
        
        buffer.unbind();
        program.disable();     
    }
    
    private float quantize(float actual) {
        return actual < 0 ? (float) Math.ceil(actual) :
                            (float) Math.floor(actual);
    }
    
}
