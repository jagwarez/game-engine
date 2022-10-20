package jagwarez.game.asset.model.reader.obj;

import jagwarez.game.asset.AssetReader;
import jagwarez.game.asset.model.Effect;
import jagwarez.game.asset.model.Material;
import jagwarez.game.asset.model.Mesh;
import jagwarez.game.asset.model.Model;
import jagwarez.game.asset.model.Texture;
import jagwarez.game.asset.model.Vertex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 *
 * @author jacob
 */
public class OBJModelReader implements AssetReader<Model> {
    
    private final Map<String,Material> materials;
    
    public OBJModelReader() {
        materials = new HashMap<>();
    }
    
    @Override
    public Model read(File file) throws IOException {
        
        Model model = new Model(file.getName().replaceAll("\\.\\w+$",""));
        Mesh mesh = null;
        Mesh.Group group = null;
        
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));

            ArrayList<Vertex> positions = new ArrayList<>();
            ArrayList<Vector4f> normals = new ArrayList<>();
            ArrayList<Vector2f> coords = new ArrayList<>();
            Vertex vertex;

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                
                String[] fields = line.split("\\s+");

                if(fields.length > 1) {
                    
                    switch(fields[0].toLowerCase()) {
                        
                        case "mtllib":
                        
                            readMaterials(new File(file.getParentFile(), fields[1]));
                        
                            break;
                        
                        case "usemtl":
                        
                            if(mesh == null) {
                                mesh = new Mesh(model.name);
                                model.meshes.put(mesh.name, mesh);
                            }

                            if(group == null) {
                                group = new Mesh.Group(mesh.groups.size());
                                mesh.groups.add(group);
                            }

                            Material material = materials.get(fields[1]);                    
                            if(material != null)
                                group.material.effects.putAll(material.effects);
                            
                            break;
                            
                        case "o":
                        
                            mesh = new Mesh(fields[1]);
                            model.meshes.put(mesh.name, mesh);
                            
                            break;
                        
                        case "g":
                        
                            group = new Mesh.Group(mesh.groups.size());
                            mesh.groups.add(group);
                            
                            break;
                            
                        case "v":
                            
                            vertex = new Vertex(positions.size());

                            vertex.position.x = Float.parseFloat(fields[1]);
                            vertex.position.y = Float.parseFloat(fields[2]);
                            vertex.position.z = Float.parseFloat(fields[3]);
                            vertex.position.w = 1f;

                            positions.add(vertex);

                            break;
                            
                        case "vn":
                            
                            Vector4f normal = new Vector4f();
                            normal.x = Float.parseFloat(fields[1]);
                            normal.y = Float.parseFloat(fields[2]);
                            normal.z = Float.parseFloat(fields[3]);

                            normals.add(normal);
                            
                            break;

                        case "vt":
                            
                            Vector2f uv = new Vector2f();
                            uv.x = Float.parseFloat(fields[1]);
                            uv.y = Float.parseFloat(fields[2]);

                            coords.add(uv);
                            
                            break;

                        case "f":
                        
                            if(mesh == null) {
                                mesh = new Mesh(model.name);
                                model.meshes.put(mesh.name, mesh);
                            }

                            if(group == null) {
                                group = new Mesh.Group(mesh.groups.size());
                                mesh.groups.add(group);
                            }

                            if(fields.length == 4) {

                                for(int vi = 0; vi < 3; vi++) {
                                    String[] field = fields[vi+1].split("/");
                                    vertex = positions.get(Integer.parseInt(field[0])-1);

                                    if(field.length > 1)
                                        vertex.coordinate.set(coords.get(Integer.parseInt(field[1])-1));

                                    if(field.length > 2)
                                        vertex.normal.set(normals.get(Integer.parseInt(field[2])-1));

                                    group.vertices.add(vertex);
                                }

                            } else if(fields.length == 5) { // quad
                                Vertex[] face1 = new Vertex[3];
                                Vertex[] face2 = new Vertex[3];

                                for(int vi = 0; vi < 4; vi++) {
                                    String[] field = fields[vi+1].split("/");
                                    vertex = positions.get(Integer.parseInt(field[0])-1);

                                    if(field.length > 1)
                                        vertex.coordinate.set(coords.get(Integer.parseInt(field[1])-1));

                                    if(field.length > 2)
                                        vertex.normal.set(normals.get(Integer.parseInt(field[2])-1));

                                    switch (vi) {
                                        case 0:
                                            face1[0] = vertex;
                                            face2[0] = vertex;
                                            break;
                                        case 1:
                                            face1[1] = vertex;
                                            break;
                                        case 2:
                                            face1[2] = vertex;
                                            face2[1] = vertex;                                 
                                            break;
                                        default:
                                            face2[2] = vertex;
                                            break;
                                    }
                                }

                                group.vertices.add(face1[0]);
                                group.vertices.add(face1[1]);
                                group.vertices.add(face1[2]);

                                group.vertices.add(face2[0]);
                                group.vertices.add(face2[1]);
                                group.vertices.add(face2[2]);

                            }
                            
                            break;
                    }    
                }
            }
            
        } finally {
            if(reader != null) {
                try { reader.close(); }
                catch(IOException ex) { ex.printStackTrace(System.err); }
            }
        }
        
        return model;
    }
    
    public void readMaterials(File mtl) throws IOException {
        
        Material material = null;
        BufferedReader reader = null;
        
        try {
            
            reader = new BufferedReader(new FileReader(mtl));
            
            for(String line = reader.readLine(); line != null; line = reader.readLine()) {   
                
                String fields[] = line.split("\\s+");
                
                switch(fields[0].toLowerCase()) {
                    
                    case "newmtl":

                        material = new Material();
                        materials.put(fields[1], material);
                        
                        break;
                        
                    case "map_kd":
                        
                        if(material != null) {
                            Texture texture = new Texture(new File(mtl.getParentFile(), fields[1]));
                            material.effects.put(Effect.Parameter.DIFFUSE, texture);
                        }
                        
                        break;
                }
            }
        } finally {
            if(reader != null) {
                try { reader.close(); }
                catch(IOException ex) { ex.printStackTrace(System.err); }
            }
        }
        
    }
}
