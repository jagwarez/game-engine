/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.pipeline;

import jagwarez.game.Assets.Models;
import jagwarez.game.Pipeline;
import jagwarez.game.Shader;
import jagwarez.game.asset.Effect;
import jagwarez.game.asset.Bone;
import jagwarez.game.asset.Color;
import jagwarez.game.asset.Mesh;
import jagwarez.game.asset.Model;
import jagwarez.game.asset.Texture;
import jagwarez.game.asset.Vertex;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

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
        
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(models.animationVertex*3);
        FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(models.animationVertex*3);
        FloatBuffer texcoordBuffer = BufferUtils.createFloatBuffer(models.animationVertex*2);
        IntBuffer boneBuffer = BufferUtils.createIntBuffer(models.animationVertex*4);
        FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(models.animationVertex*4);
        int groupOffset = 0;
        
        for(Model model : models.animations()) {
            
            for(Mesh mesh : model.meshes.values()) {
                
                for(Mesh.Group group : mesh.groups) {
                    
                    group.offset = groupOffset;

                    for(Vertex vertex : group.vertices) {

                        vertexBuffer.put(vertex.position.x);
                        vertexBuffer.put(vertex.position.y);
                        vertexBuffer.put(vertex.position.z);

                        normalsBuffer.put(vertex.normal.x);
                        normalsBuffer.put(vertex.normal.y);
                        normalsBuffer.put(vertex.normal.z);

                        texcoordBuffer.put(vertex.texcoord.x);
                        texcoordBuffer.put(vertex.texcoord.y);

                        ArrayList<Map.Entry<Bone,Float>> topWeights = new ArrayList<>(vertex.weights.entrySet());
                        topWeights.sort((Map.Entry<Bone,Float> a, Map.Entry<Bone,Float> b) -> b.getValue().compareTo(a.getValue()));

                        float totalWeight = 0f;
                        for(int i = 0; i < topWeights.size(); i++)
                            totalWeight += topWeights.get(i).getValue();

                        for(int i = 0; i < 4; i++) {
                            Map.Entry<Bone,Float> boneWeight = i < topWeights.size() ? topWeights.get(i) : null;
                            if(boneWeight != null) {
                                //System.out.println("vertex="+vertex.index+",bone"+i+"-"+boneWeight.getKey().name+": "+Math.min(boneWeight.getValue()/totalWeight, 1));
                                boneBuffer.put(boneWeight.getKey().index);
                                weightBuffer.put(Math.min(boneWeight.getValue()/totalWeight, 1));
                            } else {
                                boneBuffer.put(-1);
                                weightBuffer.put(0f);
                            }
                        }
                    }

                    for(Effect effect : group.material.effects.values())
                        if(effect.type == Effect.Type.TEXTURE)
                            load((Texture)effect);


                    groupOffset += group.vertices.size();
                }
            }
        }

        program.bindShader(new Shader("jagware/game/pipeline/program/animation/vs.glsl", Shader.Type.VERTEX));
        program.bindShader(new Shader("jagware/game/pipeline/program/animation/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindAttribute(1, "normal");
        program.bindAttribute(2, "texcoord");
        program.bindAttribute(3, "bones");
        program.bindAttribute(4, "weights");
        program.bindFragment(0, "color");

        buffer.bind();
        
        buffer.attribute((FloatBuffer) vertexBuffer.flip(), 3);
        buffer.attribute((FloatBuffer) normalsBuffer.flip(), 3);
        buffer.attribute((FloatBuffer) texcoordBuffer.flip(), 2);
        buffer.attribute((IntBuffer) boneBuffer.flip(), 4);
        buffer.attribute((FloatBuffer) weightBuffer.flip(), 4);
        
        buffer.unbind();
        
        return this;
    }
    
    @Override
    public void render(Model model, Matrix4f transform) {
        
        enable();
        
        program.bindUniform("transform").setMatrix4fv(transform);
        
        for(int i = 0; i < model.bones.size(); i++) {
            program.bindUniform("bone_transforms["+i+"]").setMatrix4fv(model.bones.get(i).transform);
        }
        
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

                glDrawArrays(GL_TRIANGLES, group.offset, group.vertices.size());
                //glDrawElements(GL_TRIANGLES, mesh.triangles.size()*3, GL_UNSIGNED_INT, 0);
            }
        }
        
        disable();
    }
}
