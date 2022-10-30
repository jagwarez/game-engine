package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Light;
import jagwarez.game.engine.Program;
import jagwarez.game.engine.Shader;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 *
 * @author jacob
 */
public class LightPipeline extends RenderPipeline {
    
    public static final int MAX_LIGHTS = 10;
    
    @Override
    public void load() throws Exception {
        
        FloatBuffer quad = BufferUtils.createFloatBuffer(2*6);
        quad.put(10f).put(10f);
        quad.put(10f).put(-10f);
        quad.put(-10f).put(10f);
        quad.put(-10f).put(10f);
        quad.put(-10f).put(-10f);
        quad.put(10f).put(-10f);
        
        program.attach(new Shader("jagwarez/game/engine/pipeline/program/light/vs.glsl", Shader.Type.VERTEX));
        program.attach(new Shader("jagwarez/game/engine/pipeline/program/light/fs.glsl", Shader.Type.FRAGMENT));
        program.attribute(0, "position");
        program.fragment(0, "color");
        program.link();
        
        buffer.bind();       
        buffer.attribute((FloatBuffer) quad.flip(), 2);             
        buffer.unbind();
    }

    @Override
    public void execute() throws Exception {
        program.enable();
        buffer.bind();
        
        glDisable(GL_CULL_FACE);

        render(program);
        
        glEnable(GL_CULL_FACE);
        
        buffer.unbind();
        program.disable();
    }

    @Override
    public void render(Program program) throws Exception {
        
        Matrix4f view = new Matrix4f();
        view.translate(camera.getTranslation(new Vector3f()));
                
        program.uniform("world").matrix(world);
        program.uniform("camera").matrix(camera);

        for(Light light : world.lights) {
            
            light.identity();
            light.translate(light.position);        
            light.rotateY((float)Math.toRadians(camera.rotation.y+180));
            light.rotateX((float)Math.toRadians(camera.rotation.x+180));
            
            program.uniform("transform").matrix(light);
            
            glDrawArrays(GL_TRIANGLES, 0, 6);
        }
    }
    
}
