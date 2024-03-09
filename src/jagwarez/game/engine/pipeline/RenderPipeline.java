package jagwarez.game.engine.pipeline;

import jagwarez.game.engine.Buffer;
import jagwarez.game.engine.Camera;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Graphics;
import jagwarez.game.engine.Light;
import jagwarez.game.engine.Program;
import jagwarez.game.engine.Sky;
import jagwarez.game.engine.World;
import java.util.List;
import org.joml.Matrix4x3f;
import org.joml.Vector4f;

/**
 *
 * @author jacob
 */
abstract class RenderPipeline extends TexturePipeline implements StaticPipeline {
    
    protected Graphics graphics;
    protected final Program program;
    protected final Buffer buffer;
    
    protected World world;
    protected Camera camera;
    protected Sky sky;
    protected List<Light> lights;
      
    public RenderPipeline() {  
        program = new Program();
        buffer = new Buffer();
        pipelines.put(getClass(), this);
    }
    
    @Override
    public void init(Game game) throws Exception {
        
        program.init();
        buffer.init();
        
        world = game.world;
        camera = world.camera;       
        graphics = game.graphics;
        sky = game.world.sky;
        lights = game.world.lights;
        
    }
    
    @Override
    public void destroy() throws Exception {    
        super.destroy();     
        buffer.destroy();     
    }
    
    public void render() throws Exception {
        render(program);
    }
    
    protected abstract void render(Program program) throws Exception;
    
    protected void sky() throws Exception {
        program.uniform("sky_color").rgb(sky.color);
    }
    
    protected void fog() throws Exception {
        program.uniform("fog").bool(graphics.fog);
    }
    
    protected void lights() throws Exception {
        program.uniform("light_count").integer(lights.size());
        for(int i = 0; i < lights.size(); i++) {
            Light light = lights.get(i);

            Matrix4x3f data = new Matrix4x3f();
            data.setRow(0, new Vector4f(light.position, 1f));
            data.setRow(1, light.color.rgba());
            data.setRow(2, new Vector4f(light.attenuation, light.intensity));
            
            program.uniform("lights["+i+"]").matrix(data);
        }
    }
    
}
