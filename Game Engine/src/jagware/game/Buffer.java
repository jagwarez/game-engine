/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
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
    
    private ArrayList<Integer> buffers;
    private int attributeIndex = 0;
    
    public Buffer() {
        Game.log("Creating buffer");
        this.vaoId = glGenVertexArrays();
        buffers = new ArrayList<>();
    }
    
    public void bind() {
        
        Game.log("Enabling array "+vaoId);
        glBindVertexArray(vaoId);
            
        for(int i = 0; i < attributeIndex; i++) {
            Game.log("Enabling attribute "+i);
            glEnableVertexAttribArray(i);
        }
    }
    
    public void unbind() {
       
        for(int i = 0; i < attributeIndex; i++) {
            Game.log("Disabling attribute "+i);
            glDisableVertexAttribArray(i-1);
        }
        
        glBindVertexArray(0);

    }
     
    public void elements(IntBuffer buffer) {
        int vboId = glGenBuffers();
        
        buffers.add(vboId);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }
    
    public void attribute(FloatBuffer buffer, int stride) {
        int vboId = glGenBuffers();
        
        buffers.add(vboId);
        
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeIndex++, stride, GL_FLOAT, false, 0, 0L);
    }
    
    public void attribute(IntBuffer buffer, int stride) {
        int vboId = glGenBuffers();
        
        buffers.add(vboId);
        
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeIndex++, stride, GL_INT, false, 0, 0L);
    }
    
    public void destroy() {
        
        glBindVertexArray(vaoId);
            
        for(int vboId : buffers)
            glDeleteBuffers(vboId);

        Game.log("Buffer destroyed");
    }
    
}
