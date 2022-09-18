/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagwarez.game;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

/**
 *
 * @author Jake
 */
public class Program {
   
    public final Map<Integer,Shader> shaders = new HashMap<>();
    public final Map<String,Uniform> uniforms = new HashMap<>();
    
    private int id;
    private boolean linked = false;
    
    public void create() {
        Game.log("Creating program");
        id = glCreateProgram(); 
    }
    
    public void enable() {
        
        if(!linked) {
            glLinkProgram(id);
            int link = glGetProgrami(this.id, GL_LINK_STATUS);
            String programLog = glGetProgramInfoLog(id);
            if (programLog.trim().length() > 0) {
                System.err.println(programLog);
            }
            if (link == 0) {
                throw new AssertionError("Could not link program");
            }
            
            linked = true;
        }
        
        glUseProgram(id); 
    }
    
    public void disable() { 
        glUseProgram(0);
    }
    
    public void bindShader(Shader shader) {
        if(!shaders.containsKey(shader.id)) {
            glAttachShader(this.id, shader.id);
            shaders.put(shader.id, shader);
        }
    }
    
    public void unbindShader(Shader shader) {
        if(shaders.containsKey(shader.id)) {
            glDetachShader(this.id, shader.id);
            shaders.remove(shader.id);
        }
    }
    
    public void bindAttribute(int index, String name) {
        glBindAttribLocation(this.id, index, name);
    }
    
    public void bindFragment(int index, String name) {
        glBindFragDataLocation(this.id, index, name);
    }

    public Uniform bindUniform(String name) {
        
        if(uniforms.containsKey(name))
            return uniforms.get(name);
        
        int uniformId = glGetUniformLocation(this.id, name);
        Uniform uniform = new Uniform(uniformId);
        uniforms.put(name, uniform);
        
        return uniform;
    }
    
    public static class Uniform {
        
        public final int id;
        
        private static final FloatBuffer M4FBUFFER = BufferUtils.createFloatBuffer(16);
        
        public Uniform(int id) {
            this.id = id;
        }
        
        public void setBool(boolean b) {
            glUniform1i(id, b ? 1 : 0);
        }
        
        public void set1i(int i) {
            glUniform1i(id, i);
        }
        
        public void set1f(float f1) {
            glUniform1f(id, f1);
        }
        
        public void set2f(float f1, float f2) {
            glUniform2f(id, f1, f2);
        }
        
        public void set3f(float f1, float f2, float f3) {
            glUniform3f(id, f1, f2, f3);
        }
        
        public void set4f(float f1, float f2, float f3, float f4) {
            glUniform4f(id, f1, f2, f3, f4);
        }
        
        public void setMatrix4fv(Matrix4f matrix) {
            setMatrix4fv(matrix, false);
        }
        
        public void setMatrix4fv(Matrix4f matrix, boolean transpose) {
            glUniformMatrix4fv(id, transpose, matrix.get(M4FBUFFER));
        }
    }
}