/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.pipeline;

import jagware.game.Assets.Models;
import jagware.game.Game;
import jagware.game.Pipeline;
import jagware.game.Shader;
import jagware.game.asset.Effect;
import jagware.game.asset.Joint;
import jagware.game.asset.Mesh;
import jagware.game.asset.Model;
import jagware.game.asset.Texture;
import jagware.game.asset.Triangle;
import jagware.game.asset.Vertex;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL32.glDrawElementsBaseVertex;

/**
 *
 * @author jacob
 */
public class SkeletonPipeline extends Pipeline<Model> {
    
    private final Models models;
    
    public SkeletonPipeline(Models models) {
        this.models = models;
    }
    
    @Override
    public SkeletonPipeline load() throws Exception {
        Game.log("Indexes="+models.animationIndex);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(models.animationIndex);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(models.animationVertex*3);
        FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(models.animationVertex*3);
        FloatBuffer texcoordBuffer = BufferUtils.createFloatBuffer(models.animationVertex*2);
        IntBuffer jointBuffer = BufferUtils.createIntBuffer(models.animationVertex*4);
        FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(models.animationVertex*4);

        int meshOffset = 0;
        for(Model model : models.animations()) {

            for(Mesh mesh : model.meshes.values()) {

                mesh.index = meshOffset;

                for(Triangle triangle : mesh.triangles) {
                    for(Vertex vertex : triangle.vertices)
                        indexBuffer.put(vertex.index);
                }

                for(Vertex vertex : mesh.vertices) {

                    vertexBuffer.put(vertex.position.x);
                    vertexBuffer.put(vertex.position.y);
                    vertexBuffer.put(vertex.position.z);

                    normalsBuffer.put(vertex.normal.x);
                    normalsBuffer.put(vertex.normal.y);
                    normalsBuffer.put(vertex.normal.z);

                    texcoordBuffer.put(vertex.texcoord.x);
                    texcoordBuffer.put(vertex.texcoord.y);

                    ArrayList<Map.Entry<Joint,Float>> topJoints = new ArrayList<>(vertex.weights.entrySet());
                    topJoints.sort((Map.Entry<Joint,Float> a, Map.Entry<Joint,Float> b) -> b.getValue().compareTo(a.getValue()));
                    
                    float totalWeight = 0f;
                    for(int i = 0; i < 4; i++) {
                        totalWeight += i < topJoints.size() ? topJoints.get(i).getValue() : 0f;
                    }
                    
                    Map.Entry<Joint,Float>[] jointWeights = new Map.Entry[4];
                    for(int i = 0; i < jointWeights.length; i++) {
                        jointWeights[i] = i < topJoints.size() ? topJoints.get(i) : null;
                        if(jointWeights[i] != null) {
                            //System.out.println("joint-"+i+": "+jointWeights[i].getKey().index+"="+Math.min(jointWeights[i].getValue()/totalWeight, 1));
                            jointBuffer.put(jointWeights[i].getKey().index);
                            weightBuffer.put(Math.min(jointWeights[i].getValue()/totalWeight, 1));
                        } else {
                            jointBuffer.put(-1);
                            weightBuffer.put(0f);
                        }
                    }
                }

                meshOffset += mesh.triangles.size()*3;
            }
            
            for(Texture texture : model.textures.values())
                load(texture);
        }

        program.bindShader(new Shader("jagware/game/pipeline/program/animation/vs.glsl", Shader.Type.VERTEX));
        program.bindShader(new Shader("jagware/game/pipeline/program/animation/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindAttribute(1, "normal");
        program.bindAttribute(2, "texcoord");
        program.bindAttribute(3, "joints");
        program.bindAttribute(4, "weights");
        program.bindFragment(0, "color");

        buffer.enable();
        
        buffer.indices((IntBuffer) indexBuffer.flip());
        buffer.vertices((FloatBuffer) vertexBuffer.flip());
        buffer.normals((FloatBuffer) normalsBuffer.flip());
        buffer.texcoords((FloatBuffer) texcoordBuffer.flip());
        buffer.joints((IntBuffer) jointBuffer.flip());
        buffer.weights((FloatBuffer) weightBuffer.flip());
        
        return this;
    }
    
    @Override
    public void render(Model model, Matrix4f transform) {
        
        enable();
        
        program.bindUniform("transform").setMatrix4fv(transform);
        
        for(int i = 0; i < model.joints.size(); i++) {
            program.bindUniform("joint_transforms["+i+"]").setMatrix4fv(model.joints.get(i).transform, false);
        }
        
        //program.bindUniform("useDiffuseMap").setBool(false);
        //program.bindUniform("diffuseColor").set4f(1,1,1,1);
        //Game.log("Drawing "+model.name);
        for(Mesh mesh : model.meshes.values()) {
            
            Effect diffuse = mesh.material.effects.get(Effect.Parameter.DIFFUSE);
            if(diffuse.type == Effect.Type.TEXTURE) {
                Texture diffuseMap = (Texture) diffuse;
                
                glActiveTexture(GL_TEXTURE0 + 0);
                glBindTexture(GL_TEXTURE_2D, diffuseMap.id);
                
                program.bindUniform("diffuseMap").set1i(0);
            }
            
            int indices = mesh.triangles.size()*3;
            glDrawElementsBaseVertex(GL_TRIANGLES, indices, GL_UNSIGNED_INT​, mesh.index * 4, mesh.index);
            //glDrawElements(GL_TRIANGLES, mesh.triangles.size()*3, GL_UNSIGNED_INT, 0);
        }
        
        disable();
    }
}
