/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.pipeline;

import jagwarez.game.Pipeline;
import jagwarez.game.asset.Terrain;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;

/**
 *
 * @author jacob
 */
public class TerrainPipeline extends Pipeline<Terrain> {
    
    @Override
    public TerrainPipeline load() throws Exception {
        
        int size = 512;
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(size*size*3);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer((size-1)*(size-1)*6);

        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {

                vertexBuffer.put(z);
                vertexBuffer.put(x);

                if(x > 0 && z > 0) {

                    indexBuffer.put((x-1) * size + (z-1));
                    indexBuffer.put(x * size + (z-1));
                    indexBuffer.put(x * size + z);
                    
                    indexBuffer.put(x * size + z);
                    indexBuffer.put((x-1) * size + z);
                    indexBuffer.put((x-1) * size + (z-1));

                }
            }
        }

        indexBuffer.flip();
        vertexBuffer.flip();
        
        buffer.bind();
        
        buffer.elements(indexBuffer);
        buffer.attribute(vertexBuffer, 3);
        
        buffer.unbind();
        
        return this;
    }

    @Override
    public void render(Terrain asset, Matrix4f transform) throws Exception {
        
       enable();
       
       //glDrawElements(GL_TRIANGLES, mesh.triangles.size()*3, GL_UNSIGNED_INT, 0);
       
       disable();
       
    }
    
}
