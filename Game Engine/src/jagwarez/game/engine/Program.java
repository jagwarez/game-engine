package jagwarez.game.engine;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Matrix4x3f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL21.glUniformMatrix4x3fv;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

/**
 *
 * @author jacob
 */
public class Program {
   
    public final Map<Integer,Shader> shaders = new HashMap<>();
    public final Map<String,Uniform> uniforms = new HashMap<>();
    
    private int id;
    private boolean linked = false;
    
    public void init() {
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
    
    public void attach(Shader shader) {
        if(!shaders.containsKey(shader.id)) {
            glAttachShader(this.id, shader.id);
            shaders.put(shader.id, shader);
        }
    }
    
    public void detach(Shader shader) {
        if(shaders.containsKey(shader.id)) {
            glDetachShader(this.id, shader.id);
            shaders.remove(shader.id);
        }
    }
    
    public void attribute(int index, String name) {
        glBindAttribLocation(this.id, index, name);
    }
    
    public void fragment(int index, String name) {
        glBindFragDataLocation(this.id, index, name);
    }

    public Uniform uniform(String name) {
        
        if(uniforms.containsKey(name))
            return uniforms.get(name);
        
        int uniformId = glGetUniformLocation(this.id, name);
        Uniform uniform = new Uniform(uniformId);
        uniforms.put(name, uniform);
        
        return uniform;
    }
    
    public static class Uniform {
        
        public final int id;
        
        private static final FloatBuffer V2F = BufferUtils.createFloatBuffer(2);
        private static final FloatBuffer V3F = BufferUtils.createFloatBuffer(3);
        private static final FloatBuffer V4F = BufferUtils.createFloatBuffer(4);
        private static final FloatBuffer M3F = BufferUtils.createFloatBuffer(9);
        private static final FloatBuffer M4X3F = BufferUtils.createFloatBuffer(12);
        private static final FloatBuffer M4F = BufferUtils.createFloatBuffer(16);
        
        public Uniform(int id) {
            this.id = id;
        }
        
        public void bool(boolean b) {
            glUniform1i(id, b ? 1 : 0);
        }
        
        public void int1(int i) {
            glUniform1i(id, i);
        }
        
        public void float1(float f1) {
            glUniform1f(id, f1);
        }
        
        public void float2(float f1, float f2) {
            glUniform2f(id, f1, f2);
        }
        
        public void vec2f(Vector2f vector) {
            glUniform2fv(id, vector.get(V2F));
        }
        
        public void float3(float f1, float f2, float f3) {
            glUniform3f(id, f1, f2, f3);
        }
        
        public void vec3f(Vector3f vector) {
            glUniform3fv(id, vector.get(V3F));
        }
        
        public void float4(float f1, float f2, float f3, float f4) {
            glUniform4f(id, f1, f2, f3, f4);
        }
        
        public void vec4f(Vector4f vector) {
            glUniform4fv(id, vector.get(V4F));
        }
        
        public void mat3f(Matrix3f matrix) {
            mat3f(matrix, false);
        }
        
        public void mat3f(Matrix3f matrix, boolean transpose) {
            glUniformMatrix3fv(id, transpose, matrix.get(M3F));
        }
        
        public void mat4f(Matrix4f matrix) {
            mat4f(matrix, false);
        }
        
        public void mat4f(Matrix4f matrix, boolean transpose) {
            glUniformMatrix4fv(id, transpose, matrix.get(M4F));
        }
        
        public void mat4x3f(Matrix4x3f matrix) {
            mat4x3f(matrix, false);
        }
        
        public void mat4x3f(Matrix4x3f matrix, boolean transpose) {
            glUniformMatrix4x3fv(id, transpose, matrix.get(M4X3F));
        }
        
    }
}