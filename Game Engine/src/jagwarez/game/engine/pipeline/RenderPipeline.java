package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Buffer;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Graphics;
import jagwarez.game.engine.Light;
import jagwarez.game.engine.Player;
import jagwarez.game.engine.Program;
import java.util.List;
import org.joml.Matrix4x3f;
import org.joml.Vector4f;

/**
 *
 * @author jacob
 */
abstract class RenderPipeline extends TexturePipeline implements SharedPipeline {
    
    protected Graphics graphics;
    protected final Program program;
    protected final Buffer buffer;
    
    private Player player;
    private List<Light> lights;
      
    public RenderPipeline() {
        
        program = new Program();
        buffer = new Buffer();
 
        programs.put(getClass(), program);
        buffers.put(getClass(), buffer);
    }
    
    @Override
    public void init(Game game) throws Exception {
        
        program.init();
        buffer.init();
        
        graphics = game.graphics;
        player = game.world.player;
        lights = game.world.lights;
    }
    
    @Override
    public void destroy() throws Exception {    
        super.destroy();     
        buffer.destroy();     
    }
    
    protected void lights() throws Exception {
        program.uniform("light_count").int1(lights.size());
        for(int i = 0; i < lights.size(); i++) {
            Light light = lights.get(i);

            Matrix4x3f data = new Matrix4x3f();
            data.setRow(0, new Vector4f(light.position, 1f));
            data.setRow(1, new Vector4f(light.color.r, light.color.g, light.color.b, light.color.a));
            data.setRow(2, new Vector4f(light.attenuation, light.intensity));
            
            program.uniform("lights["+i+"]").mat4x3f(data);
        }
    }
    
}
