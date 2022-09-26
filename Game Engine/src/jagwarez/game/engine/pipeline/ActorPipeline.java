package jagwarez.game.engine.pipeline;

import jagwarez.game.asset.model.Bone;
import jagwarez.game.asset.model.Color;
import jagwarez.game.asset.model.Effect;
import jagwarez.game.asset.model.Mesh;
import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.Texture;
import jagwarez.game.asset.model.Vertex;
import jagwarez.game.engine.Actor;
import jagwarez.game.engine.Assets;
import jagwarez.game.engine.Game;
import jagwarez.game.engine.Shader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
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
class ActorPipeline extends RenderPipeline {
    
    private Assets assets;
    private Actor player;
    private List<Actor> actors;
    
    @Override
    public void init(Game game) throws Exception {
        
        super.init(game);
        
        assets = game.assets;
        player = game.world.player;
        actors = game.world.actors;
    
    }
    
    @Override
    public void load() throws Exception {
        
        List<Model> models = new ArrayList<>();
        int indexSize = 0;
        int vertexSize = 0;
        
        for(Model model : assets.models) {
            
            if(!model.animated())
                continue;
            
            models.add(model);
            
            for(Mesh mesh : model.meshes.values())
                for(Mesh.Group group : mesh.groups) {
                    indexSize += group.indices.size();
                    vertexSize += group.vertices.size();
                }
        }
   
        IntBuffer indices = BufferUtils.createIntBuffer(indexSize);
        FloatBuffer vertices = BufferUtils.createFloatBuffer(vertexSize*3);
        FloatBuffer normals = BufferUtils.createFloatBuffer(vertexSize*3);
        FloatBuffer coords = BufferUtils.createFloatBuffer(vertexSize*2);
        IntBuffer bones = BufferUtils.createIntBuffer(vertexSize*4);
        FloatBuffer weights = BufferUtils.createFloatBuffer(vertexSize*4);
        int offset = 0;
        
        for(Model model : models) {
            
            for(Mesh mesh : model.meshes.values()) {
                
                for(Mesh.Group group : mesh.groups) {
                    
                    group.offset = offset;
                    
                    for(int index : group.indices)
                        indices.put(index);

                    for(Vertex vertex : group.vertices) {

                        vertices.put(vertex.position.x);
                        vertices.put(vertex.position.y);
                        vertices.put(vertex.position.z);

                        normals.put(vertex.normal.x);
                        normals.put(vertex.normal.y);
                        normals.put(vertex.normal.z);

                        coords.put(vertex.texcoord.x);
                        coords.put(vertex.texcoord.y);

                        ArrayList<Map.Entry<Bone,Float>> topWeights = new ArrayList<>(vertex.weights.entrySet());
                        topWeights.sort((Map.Entry<Bone,Float> a, Map.Entry<Bone,Float> b) -> b.getValue().compareTo(a.getValue()));

                        float totalWeight = 0f;
                        for(int i = 0; i < topWeights.size(); i++)
                            totalWeight += topWeights.get(i).getValue();

                        for(int i = 0; i < 4; i++) {
                            Map.Entry<Bone,Float> boneWeight = i < topWeights.size() ? topWeights.get(i) : null;
                            if(boneWeight != null) {
                                //System.out.println("vertex="+vertex.index+",bone"+i+"-"+boneWeight.getKey().name+": "+Math.min(boneWeight.getValue()/totalWeight, 1));
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

                    offset += group.indices.size();
                }
            }
        }

        program.bindShader(new Shader("jagwarez/game/engine/pipeline/program/actor/vs.glsl", Shader.Type.VERTEX));
        program.bindShader(new Shader("jagwarez/game/engine/pipeline/program/actor/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindAttribute(1, "normal");
        program.bindAttribute(2, "texcoord");
        program.bindAttribute(3, "bones");
        program.bindAttribute(4, "weights");
        program.bindFragment(0, "color");

        buffer.bind();
        
        buffer.elements((IntBuffer) indices.flip());
        buffer.attribute((FloatBuffer) vertices.flip(), 3);
        buffer.attribute((FloatBuffer) normals.flip(), 3);
        buffer.attribute((FloatBuffer) coords.flip(), 2);
        buffer.attribute((IntBuffer) bones.flip(), 4);
        buffer.attribute((FloatBuffer) weights.flip(), 4);
        
        buffer.unbind();
    }
    
    @Override
    public void process() {
        
        program.enable();
        buffer.bind();
        
        render(player);
        
        for(Actor actor : actors)
            render(actor);
        
        buffer.unbind();
        program.disable();
    }
    
    private void render(Actor actor) {
        
        Model model = actor.model;
        
        program.bindUniform("transform").setMatrix4fv(actor);
        
        for(int i = 0; i < model.bones.size(); i++)
            program.bindUniform("bone_transforms["+i+"]").setMatrix4fv(model.bones.get(i).transform);
        
        for(Mesh mesh : model.meshes.values()) {
            
            for(Mesh.Group group : mesh.groups) {
                
                Effect diffuse = group.material.effects.get(Effect.Parameter.DIFFUSE);
                
                if(diffuse != null) {
                    if(diffuse.type == Effect.Type.TEXTURE) {
                        Texture diffuseMap = (Texture) diffuse;

                        glActiveTexture(GL_TEXTURE0 + 0);
                        glBindTexture(GL_TEXTURE_2D, diffuseMap.id);
                        
                        program.bindUniform("useDiffuseMap").setBool(true);
                        program.bindUniform("diffuseMap").set1i(0);
                        
                    } else if(diffuse.type == Effect.Type.COLOR) {                    
                        Color c = (Color)diffuse;
                        program.bindUniform("diffuseColor").set4f(c.r, c.g, c.b, c.a);
                    }
                } else
                    program.bindUniform("diffuseColor").set4f(0f, 0f, 0f, 1);

                glDrawElements(GL_TRIANGLES, group.indices.size(), GL_UNSIGNED_INT, group.offset);
                
                glBindTexture(GL_TEXTURE_2D, 0);
            }
        }
        
    }
}
