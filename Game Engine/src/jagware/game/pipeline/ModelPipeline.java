/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.pipeline;

import jagware.game.Assets.Models;
import jagware.game.Pipeline;
import jagware.game.Shader;
import jagware.game.Window;
import jagware.game.asset.Mesh;
import jagware.game.asset.Model;
import jagware.game.asset.Triangle;
import jagware.game.asset.Vertex;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

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
        
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(models.modelIndex);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(models.modelVertex*3);
        FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(models.modelVertex*3);
        FloatBuffer texcoordBuffer = BufferUtils.createFloatBuffer(models.modelVertex*2);

        int meshOffset = 0;
        for(Model model : models.models()) {

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
                }

                meshOffset += mesh.triangles.size()*3;
            }
        }

        program.bindShader(new Shader("jagware/game/pipeline/program/model/vs.glsl", Shader.Type.VERTEX));
        //program.bindShader(new Shader("jagware/game/pipeline/program/model/gs.glsl", Shader.Type.GEOMETRY));
        program.bindShader(new Shader("jagware/game/pipeline/program/model/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindAttribute(1, "normal");
        program.bindAttribute(2, "texcoord");
        program.bindFragment(0, "color");

        buffer.enable();
        
        buffer.indices((IntBuffer) indexBuffer.flip());
        buffer.vertices((FloatBuffer) vertexBuffer.flip());
        buffer.normals((FloatBuffer) normalsBuffer.flip());
        buffer.texcoords((FloatBuffer) texcoordBuffer.flip());
        
        return this;
    }
    
    @Override
    public void render(Model model, Matrix4f transform) {
        
        enable();
        
        program.bindUniform("transform").setMatrix4fv(transform);
        program.bindUniform("diffuse").set4f(.5f, .5f, .5f, 1.0f);
        
        for(Mesh mesh : model.meshes.values())
            glDrawElements(GL_TRIANGLES, mesh.triangles.size()*3, GL_UNSIGNED_INT, 0);
    }
}
