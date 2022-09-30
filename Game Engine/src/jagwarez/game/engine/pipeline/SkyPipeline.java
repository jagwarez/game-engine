package jagwarez.game.engine.pipeline;

import jagwarez.game.asset.model.Effect;
import jagwarez.game.asset.model.Mesh;
import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.Texture;
import jagwarez.game.asset.model.Vertex;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Sky;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glCullFace;

/**
 *
 * @author jacob
 */
class SkyPipeline extends ModelPipeline {
    
    private Sky sky;
    
    @Override
    public void init(Game game) throws Exception {
        super.init(game);
        sky = game.world.sky;     
        game.assets.models.add(sky.model);
    }
    
    @Override
    public void load() throws Exception {
        
        Model model = sky.model;
        int indexSize = 0;
        int vertexSize = 0;
        
        if(model != null) {            
            for(Mesh mesh : model.meshes.values())
                for(Mesh.Group group : mesh.groups) {
                    indexSize += group.indices.size();
                    vertexSize += group.vertices.size();
                }
        }
        
        IntBuffer indices = BufferUtils.createIntBuffer(indexSize);
        FloatBuffer vertices = BufferUtils.createFloatBuffer(vertexSize*3);
        FloatBuffer coords = BufferUtils.createFloatBuffer(vertexSize*2);
   
        for(Mesh mesh : model.meshes.values()) {

            for(Mesh.Group group : mesh.groups) {
                
                for(int index : group.indices)
                    indices.put(index);

                for(Vertex vertex : group.vertices) {

                    vertices.put(vertex.position.x);
                    vertices.put(vertex.position.y);
                    vertices.put(vertex.position.z);

                    coords.put(vertex.texcoord.x);
                    coords.put(vertex.texcoord.y);
                }

                for(Effect effect : group.material.effects.values())
                    if(effect.type == Effect.Type.TEXTURE) {
                        Texture texture = (Texture)effect;
                        Map<Integer,Integer> parameters = texture.parameters;
                        parameters.put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                        parameters.put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                        parameters.put(GL_TEXTURE_WRAP_S, GL_REPEAT);
                        parameters.put(GL_TEXTURE_WRAP_T, GL_REPEAT);

                        texture(texture);
                    }
            }
        }
    
        program.attach(new Shader("jagwarez/game/engine/pipeline/program/sky/vs.glsl", Shader.Type.VERTEX));
        program.attach(new Shader("jagwarez/game/engine/pipeline/program/sky/fs.glsl", Shader.Type.FRAGMENT));
        program.attribute(0, "position");
        program.attribute(1, "texcoord");
        program.fragment(0, "color");

        buffer.bind();
        
        buffer.elements((IntBuffer) indices.flip());
        buffer.attribute((FloatBuffer) vertices.flip(), 3);
        buffer.attribute((FloatBuffer) coords.flip(), 2);
        
        buffer.unbind();
    }
    
    @Override
    public void process() throws Exception {
        
        program.enable();
        buffer.bind();
        
        lights();
        
        glCullFace(GL_FRONT);
        
        program.uniform("sky_color").float3(sky.color.r, sky.color.g, sky.color.b);
        
        render(sky.model, sky);
        
        glCullFace(GL_BACK);

        buffer.unbind();
        program.disable();    
    }

}
