package jagwarez.game.asset.model.reader.dae;

import jagwarez.game.asset.AssetReader;
import java.io.File;
import java.nio.FloatBuffer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author jacob
 */
abstract class DAEFileReader<A> implements AssetReader<A> {
    
    public static final String FILE_EXT = ".dae";
    
    protected File file;
    protected String name;
    protected  Document document;
    protected XPath xpath;
    
    protected void parse(File file)throws Exception {
        this.file = file;
        this.name = file.getName().toLowerCase().replace(FILE_EXT, "");
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(file);
        
        xpath = XPathFactory.newInstance().newXPath();
    }
            
    protected Matrix4f readMatrix4f(Element matrixElement) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        buffer.put(readFloatArray(matrixElement));
        buffer.flip();
        
        return new Matrix4f(buffer).transpose();
    }
    
    protected Matrix4f[] readMatrix4fArray(Element matrixElement) {
        float[] floats = readFloatArray(matrixElement);
        Matrix4f[] matrixArray = new Matrix4f[floats.length/16];
        
        for(int i = 0; i < matrixArray.length; i++) {
            FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
            for(int j = 0; j < 16; j++) {
                buffer.put(floats[16*i+j]);
            }
            buffer.flip();
            
            matrixArray[i] = new Matrix4f(buffer).transpose();
        }
        
        return matrixArray;
    }
    
    protected float[] readFloatArray(Element floatElement) {
        String[] floats = floatElement.getFirstChild().getNodeValue().split(" ");
        float[] floatArray = new float[floats.length];
        
        for(int i = 0; i < floatArray.length; i++)
            floatArray[i] = Float.parseFloat(floats[i]);
        
        return floatArray;
    }
    
    protected Element getElementById(String path, String id, Node node) throws Exception {
        return (Element) xpath.evaluate(String.format("%s[@id='%s']", path, id), node, XPathConstants.NODE);
    }
    
    protected Element getElementById(String path, String id) throws Exception {
        return getElementById(path, id, document);
    }
}
