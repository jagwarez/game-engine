package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Buffer;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Program;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Terrain;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 *
 * @author jacob
 */
public class PhysicsPipeline implements SharedPipeline {
    
    private final Program program;
    private Buffer buffer;
    
    private Terrain terrain;
    
    public PhysicsPipeline() {
        program = new Program();
        buffer = null;
    }
    
    @Override
    public void init(Game game) throws Exception {
        buffer = buffers.get(TerrainPipeline.class);
    }

    @Override
    public void load() throws Exception {
        program.bindShader(new Shader("jagwarez/game/pipeline/program/physics/vs.glsl", Shader.Type.VERTEX));
        program.bindShader(new Shader("jagwarez/game/pipeline/program/physics/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindFragment(0, "color");
    }

    @Override
    public void render() throws Exception {
        
        if(true) return;
        
        program.enable();
        buffer.bind();
        
        if(terrain.heightmap != null) {
            
            //Vector2f offset = new Vector2f((float)Math.floor(player.position.x)-Terrain.OFFSET, (float)Math.floor(player.position.z)-Terrain.OFFSET);

            glActiveTexture(GL_TEXTURE0 + 0);
            glBindTexture(GL_TEXTURE_2D, terrain.heightmap.id);

            program.bindUniform("twidth").set1f(4096);
            //program.bindUniform("offset").set2f(offset.x, offset.y);
            program.bindUniform("use_hmap").setBool(true);
            program.bindUniform("hmap").set1i(0);

        } else {
            program.bindUniform("use_hmap").setBool(false);
        }

        glDrawElements(GL_TRIANGLES, Terrain.INDEX_COUNT, GL_UNSIGNED_INT, 0);
        
        buffer.unbind();
        program.disable();
    }

    @Override
    public void destroy() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
