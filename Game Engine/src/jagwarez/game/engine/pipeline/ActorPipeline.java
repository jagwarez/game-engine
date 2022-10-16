package jagwarez.game.engine.pipeline;

import jagwarez.game.asset.model.Bone;
import jagwarez.game.asset.model.Effect;
import jagwarez.game.asset.model.Mesh;
import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.Skeleton;
import jagwarez.game.asset.model.Texture;
import jagwarez.game.asset.model.Vertex;
import jagwarez.game.engine.Actor;
import jagwarez.game.engine.Assets;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Program;
import jagwarez.game.engine.Shader;
import jagwarez.game.engine.World;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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
class ActorPipeline extends ModelPipeline {
    
    private World world;
    private Assets assets;
    private Actor player;
    private List<Actor> actors;
    
    @Override
    public void init(Game game) throws Exception {
        
        super.init(game);
        
        world = game.world;
        assets = game.assets;
        player = game.world.player;
        actors = game.world.actors;
    
    }
    
    @Override
    public void load() throws Exception {
        
        List<Model> models = new ArrayList<>();
        int vertexCount = 0;
        
        for(Model model : assets.models.values()) {
            
            if(!model.skeletal())
                continue;
            
            models.add(model);
            
            for(Mesh mesh : model.meshes.values())
                for(Mesh.Group group : mesh.groups)
                    vertexCount += group.vertices.size();
         
        }
   
        FloatBuffer vertices = BufferUtils.createFloatBuffer(vertexCount*3);
        FloatBuffer coords = BufferUtils.createFloatBuffer(vertexCount*2);
        FloatBuffer normals = BufferUtils.createFloatBuffer(vertexCount*3);
        IntBuffer bones = BufferUtils.createIntBuffer(vertexCount*4);
        FloatBuffer weights = BufferUtils.createFloatBuffer(vertexCount*4);
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

                        ArrayList<Map.Entry<Bone,Float>> topWeights = new ArrayList<>(vertex.weights.entrySet());
                        topWeights.sort((Map.Entry<Bone,Float> a, Map.Entry<Bone,Float> b) -> b.getValue().compareTo(a.getValue()));

                        float totalWeight = 0f;
                        for(int i = 0; i < topWeights.size(); i++)
                            totalWeight += topWeights.get(i).getValue();

                        for(int i = 0; i < 4; i++) {
                            Map.Entry<Bone,Float> boneWeight = i < topWeights.size() ? topWeights.get(i) : null;
                            if(boneWeight != null) {
                                bones.put(boneWeight.getKey().index);
                                weights.put(Math.min(boneWeight.getValue()/totalWeight, 1));
                            } else {
                                bones.put(-1);
                                weights.put(0f);
                            }
                        }
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

        program.attach(new Shader("jagwarez/game/engine/pipeline/program/actor/vs.glsl", Shader.Type.VERTEX));
        program.attach(new Shader("jagwarez/game/engine/pipeline/program/actor/fs.glsl", Shader.Type.FRAGMENT));
        program.attribute(0, "position");
        program.attribute(1, "texcoord");
        program.attribute(2, "normal");
        program.attribute(3, "bones");
        program.attribute(4, "weights");
        program.fragment(0, "color");       
        program.link();

        buffer.bind();       
        buffer.attribute((FloatBuffer) vertices.flip(), 3);
        buffer.attribute((FloatBuffer) coords.flip(), 2);
        buffer.attribute((FloatBuffer) normals.flip(), 3);
        buffer.attribute((IntBuffer) bones.flip(), 4);
        buffer.attribute((FloatBuffer) weights.flip(), 4);      
        buffer.unbind();
    }
    
    @Override
    public void execute() throws Exception {       
        program.enable();
        buffer.bind();
        
        sky();
        
        fog();
        
        lights();
        
        render();
        
        buffer.unbind();
        program.disable();
    }
    
    @Override
    public void render(Program program) {
        
        program.uniform("world").matrix(world);
        program.uniform("camera").matrix(world.camera);

        render(player, program);
        
        for(Actor actor : actors)
            render(actor, program);
    }
    
    private void render(Actor actor, Program program) {
        
        Model model = actor.model;
        
        if(model == null)
            return;
        
        Skeleton skeleton = model.skeleton;
        
        for(int i = 0; i < skeleton.bones.size(); i++)
            program.uniform("bone_transforms["+i+"]").matrix(skeleton.bones.get(i).transform);
        
        super.render(actor, program);
        
    }
}
