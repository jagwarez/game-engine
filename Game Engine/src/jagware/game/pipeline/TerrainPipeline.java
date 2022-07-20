/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.pipeline;

import jagware.game.Pipeline;
import jagware.game.asset.Terrain;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

/**
 *
 * @author jacob
 */
public class TerrainPipeline extends Pipeline<Terrain> {
    
    @Override
    public TerrainPipeline load() throws Exception {
        int size = 1000;
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(size*size*3);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer((size-1)*(size-1)*6);

        for(int x = 0; x < size; x++) {
            for(int z = 0; z < size; z++) {

                vertexBuffer.put(z);
                //vertexBuffer.put(heights[z][x]);
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
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
