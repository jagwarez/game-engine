/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 *
 * @author jacob
 */
public class Buffer {
    
    private int vaoId = -1;
    private int iboId = -1;
    private int vboId = -1, vbaId = -1;
    private int nboId = -1, nbaId = -1;
    private int tboId = -1, tbaId = -1;
    private int jboId = -1, jbaId = -1;
    private int wboId = -1, wbaId = -1;
    
    private int bufferIndex = 0;
    
    public Buffer() {
        Game.log("Creating buffer");
        this.vaoId = glGenVertexArrays();
    }
    
    public void enable() {
        
        Game.log("Enabling buffer "+vaoId);
        glBindVertexArray(vaoId);
            
        for(int i = 0; i < bufferIndex; i++) {
            Game.log("Enabling array "+i);
            glEnableVertexAttribArray(i);
        }
    }
     
    public void indices(IntBuffer indexBuffer) {
        iboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
    }
    
    public int vertices(FloatBuffer vertexBuffer) {
        vboId = glGenBuffers();
        vbaId = bufferIndex++;
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(vbaId, 3, GL_FLOAT, false, 0, 0L);
        return vbaId;
    }
    
    public int normals(FloatBuffer normalsBuffer) {
        nboId = glGenBuffers();
        nbaId = bufferIndex++;
        glBindBuffer(GL_ARRAY_BUFFER, nboId);
        glBufferData(GL_ARRAY_BUFFER, normalsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(nbaId, 3, GL_FLOAT, false, 0, 0L);
        return nbaId;
    }
    
    public int texcoords(FloatBuffer texcoordsBuffer) {
        tboId = glGenBuffers();
        tbaId = bufferIndex++;
        glBindBuffer(GL_ARRAY_BUFFER, tboId);
        glBufferData(GL_ARRAY_BUFFER, texcoordsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(tbaId, 2, GL_FLOAT, false, 0, 0L);
        return tbaId;
    }
    
    public int joints(IntBuffer jointBuffer) {
        jboId = glGenBuffers();
        jbaId = bufferIndex++;
        glBindBuffer(GL_ARRAY_BUFFER, jboId);
        glBufferData(GL_ARRAY_BUFFER, jointBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(jbaId, 4, GL_INT, false, 0, 0L);
        return jbaId;
    }
    
    public int weights(FloatBuffer weightBuffer) {
        wboId = glGenBuffers();
        wbaId = bufferIndex++;
        glBindBuffer(GL_ARRAY_BUFFER, wboId);
        glBufferData(GL_ARRAY_BUFFER, weightBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(wbaId, 4, GL_FLOAT, false, 0, 0L);
        return wbaId;
    }
  
    public void destroy() {
        
        glBindVertexArray(vaoId);
            
        if(iboId > 0)
            glDeleteBuffers(iboId);

        if(vboId != -1)
            glDeleteBuffers(vboId);
        
        if(nboId != -1)
            glDeleteBuffers(nboId);
        
        if(tboId != -1)
            glDeleteBuffers(tboId);
        
        if(jboId != -1)
            glDeleteBuffers(jboId);
        
        if(wboId != -1)
            glDeleteBuffers(wboId);
        
        Game.log("Buffer destroyed");
    }
    
}
