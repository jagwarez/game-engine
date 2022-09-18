/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glVertexAttribIPointer;

/**
 *
 * @author jacob
 */
public class Buffer {
    
    private final ArrayList<Integer> buffers;
    private int vaoId = -1;
    private int attributeIndex = 0;
    
    public Buffer() {
        Game.log("Creating buffer");
        buffers = new ArrayList<>();
    }
    
    public void create() {
        vaoId = glGenVertexArrays();
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
            glDisableVertexAttribArray(i);
        }
        
        glBindVertexArray(0);

    }
     
    public void elements(IntBuffer buffer) {
        int vboId = glGenBuffers();
        
        buffers.add(vboId);
        
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }
    
    public void attribute(FloatBuffer buffer, int size) {
        int vboId = glGenBuffers();
        
        buffers.add(vboId);
        System.out.println("Float buffer "+attributeIndex+": size="+size);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeIndex++, size, GL_FLOAT, false, size*Float.BYTES, 0L);
    }
    
    public void attribute(IntBuffer buffer, int size) {
        int vboId = glGenBuffers();
        
        buffers.add(vboId);
        System.out.println("Int buffer "+attributeIndex+": size="+size);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribIPointer(attributeIndex++, size, GL_INT, size*Integer.BYTES, 0L);
    }
    
    public void destroy() {
        
        glBindVertexArray(vaoId);
            
        for(int vboId : buffers)
            glDeleteBuffers(vboId);

        Game.log("Buffer destroyed");
    }
    
}
