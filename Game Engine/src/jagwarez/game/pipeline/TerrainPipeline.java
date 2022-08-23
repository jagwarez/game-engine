/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.pipeline;

import jagwarez.game.Pipeline;
import jagwarez.game.Player;
import jagwarez.game.Shader;
import jagwarez.game.Terrain;
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
public class TerrainPipeline extends Pipeline<Terrain> {
    
    private final Player player;
    
    public TerrainPipeline(Player player) {
        this.player = player;
    }
    
    @Override
    public TerrainPipeline load() throws Exception {
        
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(Terrain.Patch.VERTEX_COUNT);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(Terrain.Patch.INDEX_COUNT);
        int vertexRowCount = (Terrain.Patch.SIZE)+1;
        
        for(int x = 0; x < vertexRowCount; x++) {
            for(int z = 0; z < vertexRowCount; z++) {
                
                vertexBuffer.put(z);
                vertexBuffer.put(x);
                            
                if(x > 0 && z > 0) {

                    indexBuffer.put((z-1) * vertexRowCount + (x-1));
                    indexBuffer.put(z * vertexRowCount + (x-1));
                    indexBuffer.put(z * vertexRowCount + x);
                    
                    indexBuffer.put(z * vertexRowCount + x);
                    indexBuffer.put((z-1) * vertexRowCount + x);
                    indexBuffer.put((z-1) * vertexRowCount + (x-1));

                }
            }
        }

        program.bindShader(new Shader("jagwarez/game/pipeline/program/terrain/vs.glsl", Shader.Type.VERTEX));
        program.bindShader(new Shader("jagwarez/game/pipeline/program/terrain/fs.glsl", Shader.Type.FRAGMENT));
        program.bindAttribute(0, "position");
        program.bindFragment(0, "color");
        
        buffer.bind();
        
        buffer.elements((IntBuffer) indexBuffer.flip());
        buffer.attribute((FloatBuffer)vertexBuffer.flip(), 2);
        
        buffer.unbind();
        
        return this;
    }

    @Override
    public void render(Terrain terrain, Matrix4f camera) throws Exception {
        
        int row = (int)Math.floor(player.position.x/(Terrain.Patch.SIZE*terrain.scale));
        int col = (int)Math.floor(player.position.z/(Terrain.Patch.SIZE*terrain.scale));
        
        System.out.println("Tile row="+row+", col="+col);
        
        enable();

        for(int x = -1; x < 2; x++) {
            
            int patchX = row + x;
            
            if(patchX < 0 || patchX >= terrain.rows)
                continue;
            
            for(int y = -1; y < 2; y++) {
                
                int patchY = col + y;
                
                if(patchY < 0 || patchY >= terrain.columns)
                    continue;
                
                //System.out.println("Drawing patch x="+patchX+", y="+patchY);
                
                Terrain.Patch patch = terrain.grid[patchX][patchY];
                
                Matrix4f transform = camera.get(new Matrix4f());
                transform.translate(patch.x * Terrain.Patch.SIZE * terrain.scale, 0f, patch.y * Terrain.Patch.SIZE * terrain.scale);
                transform.scale(terrain.scale);
                
                program.bindUniform("transform").setMatrix4fv(transform);
                program.bindUniform("patch_color").set3f((float)patchX/terrain.rows, (float)patchY/terrain.columns, 0f);

                glDrawElements(GL_TRIANGLES, Terrain.Patch.INDEX_COUNT, GL_UNSIGNED_INT, 0);
            }
        }

        disable();
       
    }
    
}
