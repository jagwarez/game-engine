/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.pipeline;

import jagwarez.game.Assets.Models;
import jagwarez.game.Pipeline;
import jagwarez.game.Shader;
import jagwarez.game.asset.Mesh;
import jagwarez.game.asset.Model;
import jagwarez.game.asset.Vertex;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

/**
 *
 * @author jacob
 */
public class ModelPipeline extends Pipeline<Model> {
    
    private final Models models;
    
    public ModelPipeline(Models models) {
        this.models = models;
    }
    
    @Override
    public ModelPipeline load() throws Exception {
        
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(models.modelVertex*3);
        FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(models.modelVertex*3);
        FloatBuffer texcoordBuffer = BufferUtils.createFloatBuffer(models.modelVertex*2);
        int groupOffset = 0;
        
        for(Model model : models.models()) {

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
                    }

                    groupOffset += group.vertices.size();
                }
            }
        }

        program.bindShader(new Shader("jagware/game/pipeline/program/model/vs.glsl", Shader.Type.VERTEX));
        //program.bindShader(new Shader("jagware/game/pipeline/program/model/gs.glsl", Shader.Type.GEOMETRY));
        program.bindShader(new Shader("jagware/game/pipeline/program/model/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindAttribute(1, "normal");
        program.bindAttribute(2, "texcoord");
        program.bindFragment(0, "color");

        buffer.bind();
        
        buffer.attribute((FloatBuffer) vertexBuffer.flip(), 3);
        buffer.attribute((FloatBuffer) normalsBuffer.flip(), 3);
        buffer.attribute((FloatBuffer) texcoordBuffer.flip(), 2);
        
        buffer.unbind();
        
        return this;
    }
    
    @Override
    public void render(Model model, Matrix4f transform) {
        
        enable();
        
        program.bindUniform("transform").setMatrix4fv(transform);
        program.bindUniform("diffuse").set4f(.5f, .5f, .5f, 1.0f);
        
        //for(Mesh mesh : model.meshes.values())
            //glDrawElements(GL_TRIANGLES, mesh.triangles.size()*3, GL_UNSIGNED_INT, 0);
    }
}
