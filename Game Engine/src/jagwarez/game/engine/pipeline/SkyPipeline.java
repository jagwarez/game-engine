package jagwarez.game.engine.pipeline;

import jagwarez.game.asset.model.Texture;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Sky;
import jagwarez.game.engine.World;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;

/**
 *
 * @author jacob
 */
class SkyPipeline extends RenderPipeline {
    
    private World world;
    private Sky sky;
    
    @Override
    public void init(Game game) throws Exception {
        
        super.init(game);
        
        world = game.world;
        sky = world.sky;
    }

    @Override
    public void load() throws Exception {
        
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(Sky.SKYBOX.length);

        for(int i = 0; i < Sky.SKYBOX.length; i++)
            vertexBuffer.put(Sky.SKYBOX[i]);
        
        sky.cubemap.id = glGenTextures();
        glBindTexture(GL_TEXTURE_CUBE_MAP, sky.cubemap.id);
        texture(sky.cubemap);
        
        for(int i = 0; i < sky.textures.length; i++) {
            
            Texture texture = sky.textures[i];
            ByteBuffer imageBuffer = texture.buffer();
            
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA8, texture.width, texture.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);       
        }
        
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
        
        program.bindShader(new Shader("jagwarez/game/engine/pipeline/program/skybox/vs.glsl", Shader.Type.VERTEX));
        program.bindShader(new Shader("jagwarez/game/engine/pipeline/program/skybox/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindFragment(0, "color");

        buffer.bind();
        
        buffer.attribute((FloatBuffer) vertexBuffer.flip(), 3);
        
        buffer.unbind();
    }

    @Override
    public void execute() throws Exception {
        
        program.enable();
        buffer.bind();

        program.bindUniform("transform").setMatrix4fv(sky);
        program.bindUniform("sky_color").set3f(world.sky.color.r, world.sky.color.g, world.sky.color.b);
           
        glDepthFunc(GL_LEQUAL);
        glBindTexture(GL_TEXTURE_CUBE_MAP, sky.cubemap.id);
        glDrawArrays(GL_TRIANGLES, 0, 36);
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
        glDepthFunc(GL_LESS);
        
        buffer.unbind();
        program.disable();           
    }

}
