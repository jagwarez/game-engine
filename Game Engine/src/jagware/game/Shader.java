/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagware.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32C.GL_GEOMETRY_SHADER;

/**
 *
 * @author Jake
 */
public class Shader {
    
    public final int id;
    public final Type type;
    
    public Shader(String source, Type type) throws IOException {
        this(new File(URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource(source).getFile(), "UTF-8")), type);
    }
    
    public Shader(File source, Type type) throws IOException {
        FileInputStream fis = null;
        FileChannel fc = null;
        
        try {
            
            fis = new FileInputStream(source);
            fc =  fis.getChannel();
            ByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            
            this.id = create(buffer, type.glType);
            
        } finally {
            if(fc != null)
                try {  fc.close(); } catch(IOException ex) { ex.printStackTrace(System.err); }
            if(fis != null)
                try {  fis.close(); } catch(IOException ex) { ex.printStackTrace(System.err); }
        }
        
        this.type = type;
    }
      
    private int create(ByteBuffer source, int type) {
        int shaderId = glCreateShader(type);
        //ByteBuffer buffer = BufferUtils.createByteBuffer(source.length).put(source);
            
        PointerBuffer strings = BufferUtils.createPointerBuffer(1);
        IntBuffer lengths = BufferUtils.createIntBuffer(1);

        strings.put(0, source);
        lengths.put(0, source.remaining());

        glShaderSource(shaderId, strings, lengths);

        glCompileShader(shaderId);
        int compiled = glGetShaderi(shaderId, GL_COMPILE_STATUS);
        String shaderLog = glGetShaderInfoLog(shaderId);
        if (shaderLog.trim().length() > 0) {
            System.err.println(shaderLog);
        }
        if (compiled == 0) {
            throw new AssertionError("Could not compile shader");
        }
        
        return shaderId;
    }
    
    public enum Type {
        VERTEX(GL_VERTEX_SHADER), 
        GEOMETRY(GL_GEOMETRY_SHADER), 
        FRAGMENT(GL_FRAGMENT_SHADER);
        
        protected final int glType;
        private Type(int glType) {
            this.glType = glType;
        }
    }
    
}
