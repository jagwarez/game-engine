/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.pipeline;

import jagwarez.game.Pipeline;
import jagwarez.game.Player;
import jagwarez.game.Shader;
import jagwarez.game.Terrain;
import jagwarez.game.World;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
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
public class TerrainPipeline extends Pipeline<Terrain> {
    
    private final World world;
    private final Player player;
    
    public TerrainPipeline(World world) {
        this.world = world;
        this.player = world.player;
    }
    
    @Override
    public TerrainPipeline load() throws Exception {
        
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(Terrain.Patch.VERTEX_COUNT);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(Terrain.Patch.INDEX_COUNT);
        int vertexRowCount = (Terrain.Patch.SIZE);
        
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
        
        Map<Integer,Integer> parameters = new HashMap<>();
                    parameters.put(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
                    parameters.put(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                    parameters.put(GL_TEXTURE_WRAP_S, GL_REPEAT);
                    parameters.put(GL_TEXTURE_WRAP_T, GL_REPEAT);
        
        for(int row = 0; row < world.terrain.rows; row++)
            for(int col = 0; col < world.terrain.columns; col++)
                texture(world.terrain.grid[row][col].heightmap, parameters);
        
        buffer.bind();
        
        buffer.elements((IntBuffer) indexBuffer.flip());
        buffer.attribute((FloatBuffer)vertexBuffer.flip(), 2);
        
        buffer.unbind();
        
        return this;
    }

    @Override
    public void render(Terrain terrain, Matrix4f transform) throws Exception {
        
        int row = (int)Math.floor(player.position.x/((Terrain.Patch.WIDTH)*terrain.scale));
        int col = (int)Math.floor((player.position.z+10f)/((Terrain.Patch.WIDTH)*terrain.scale));
        
        //System.out.println("row="+row+", col="+col+", x="+player.position.x+", z="+player.position.z);
        
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
                
                transform.identity();
                transform.translate(patch.x, 0f, patch.y);
                transform.scale(terrain.scale);
                
                program.bindUniform("camera").setMatrix4fv(world);
                program.bindUniform("transform").setMatrix4fv(transform);
                program.bindUniform("sky_color").set3f(0.1f, 0.1f, .1f);
                program.bindUniform("patch_color").set3f((float)patchX/terrain.rows, (float)patchY/terrain.columns, 0f);

                if(patch.heightmap != null) {
                    
                    glActiveTexture(GL_TEXTURE0 + 0);
                    glBindTexture(GL_TEXTURE_2D, patch.heightmap.id);
                    
                    program.bindUniform("use_hmap").setBool(true);
                    program.bindUniform("hmap").set1i(0);
                    
                } else {
                    program.bindUniform("use_hmap").setBool(false);
                }
                
                glDrawElements(GL_TRIANGLES, Terrain.Patch.INDEX_COUNT, GL_UNSIGNED_INT, 0);
            }
        }

        disable();
       
    }
    
}
