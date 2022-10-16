package jagwarez.game.engine.pipeline;

import jagwarez.game.asset.model.Effect;
import jagwarez.game.asset.model.Mesh;
import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.Texture;
import jagwarez.game.asset.model.Vertex;
import jagwarez.game.engine.Assets;
import jagwarez.game.engine.Entity;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.Sky;
import jagwarez.game.engine.World;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;

/**
 *
 * @author jacob
 */
class EntityPipeline extends ModelPipeline {
    
    private World world;
    private Sky sky;
    private Assets assets;
    
    @Override
    public void init(Game game) throws Exception {
        
        super.init(game);
        
        world = game.world;
        sky = game.world.sky;
        assets = game.assets;

    }
    
    @Override
    public void load() throws Exception {
        
        List<Model> models = new ArrayList<>();
        int vertexCount = 0;
        
        for(Model model : assets.models.values()) {
            
            if(model.skeletal())
                continue;
            
            models.add(model);
            
            for(Mesh mesh : model.meshes.values())
                for(Mesh.Group group : mesh.groups)
                    vertexCount += group.vertices.size();

        }
        
        FloatBuffer vertices = BufferUtils.createFloatBuffer(vertexCount*3);
        FloatBuffer coords = BufferUtils.createFloatBuffer(vertexCount*2);
        FloatBuffer normals = BufferUtils.createFloatBuffer(vertexCount*3);
        
        int offset = 0;
        
        for(Model model : models) {

            for(Mesh mesh : model.meshes.values()) {

                for(Mesh.Group group : mesh.groups) {
                    
                    group.offset = offset;

                    for(Vertex vertex : group.vertices) {

                        vertices.put(vertex.position.x);
                        vertices.put(vertex.position.y);
                        vertices.put(vertex.position.z);
                        
                        coords.put(vertex.coordinate.x);
                        coords.put(vertex.coordinate.y);

                        normals.put(vertex.normal.x);
                        normals.put(vertex.normal.y);
                        normals.put(vertex.normal.z);
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

                    offset += group.vertices.size();
                }
            }
        }

        program.attach(new Shader("jagwarez/game/engine/pipeline/program/entity/vs.glsl", Shader.Type.VERTEX));
        program.attach(new Shader("jagwarez/game/engine/pipeline/program/entity/fs.glsl", Shader.Type.FRAGMENT));
        program.attribute(0, "position");
        program.attribute(1, "texcoord");
        program.attribute(2, "normal");
        program.fragment(0, "color");

        buffer.bind();
        
        buffer.attribute((FloatBuffer) vertices.flip(), 3);
        buffer.attribute((FloatBuffer) coords.flip(), 2);
        buffer.attribute((FloatBuffer) normals.flip(), 3);
        
        buffer.unbind();  
        
    }
    
    @Override
    public void execute() throws Exception {   
        program.enable();
        buffer.bind();
        
        lights();
        
        for(Entity entity : world.entities)
            render(entity.model, entity);

        buffer.unbind();
        program.disable();
    }
    
    
}
