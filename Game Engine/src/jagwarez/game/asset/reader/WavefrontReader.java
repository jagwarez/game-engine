/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jagwarez.game.asset.reader;

import jagwarez.game.asset.Mesh;
import jagwarez.game.asset.Model;
import jagwarez.game.asset.Vertex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 *
 * @author Jake
 */
public class WavefrontReader implements AssetReader<Model> {
    
    private File objFile;
    
    public WavefrontReader(File file) {
        this.objFile = file;
    }
    
    @Override
    public Model read() throws IOException {
        
        Model model = new Model(objFile.getName().replaceAll("\\.\\w+$",""));
        Mesh mesh = null;
        Mesh.Group group = null;
        
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(objFile));

            ArrayList<Vertex> vertices = new ArrayList<>();
            ArrayList<Vector4f> normals = new ArrayList<>();
            ArrayList<Vector2f> uvcoords = new ArrayList<>();

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {              
                String[] fields = line.split("\\s+");

                if(fields.length > 1) {
                    
                    if(fields[0].equalsIgnoreCase("o")) {
                        
                        mesh = new Mesh(fields[1]);
                        model.meshes.put(mesh.name, mesh);
                        
                    } else if(fields[0].equalsIgnoreCase("v")) {
                        Vertex vertex = new Vertex(vertices.size());

                        vertex.position.x = Float.parseFloat(fields[1]);
                        vertex.position.y = Float.parseFloat(fields[2]);
                        vertex.position.z = Float.parseFloat(fields[3]);
                        vertex.position.w = 1f;

                        vertices.add(vertex);

                    } else if(fields[0].equalsIgnoreCase("vn")) {
                        Vector4f normal = new Vector4f();
                        normal.x = Float.parseFloat(fields[1]);
                        normal.y = Float.parseFloat(fields[2]);
                        normal.z = Float.parseFloat(fields[3]);

                        normals.add(normal);

                    } else if(fields[0].equalsIgnoreCase("vt")) {
                        Vector2f uv = new Vector2f();
                        uv.x = Float.parseFloat(fields[1]);
                        uv.y = Float.parseFloat(fields[2]);

                        uvcoords.add(uv);

                    } else if(fields[0].equalsIgnoreCase("f")) {
                        
                        if(mesh == null) {
                            mesh = new Mesh(model.name);
                            model.meshes.put(mesh.name, mesh);
                        }
                        
                        if(group == null) {
                            group = new Mesh.Group(mesh.groups.size());
                            mesh.groups.add(group);
                        }
                        
                        if(fields.length == 4) {
                            Vertex[] face = new Vertex[3];

                            for(int vi = 0; vi < 3; vi++) {
                                String[] field = fields[vi+1].split("/");
                                Vertex vertex = vertices.get(Integer.parseInt(field[0])-1);

                                if(field.length > 1)
                                    vertex.texcoord.set(uvcoords.get(Integer.parseInt(field[1])-1));

                                if(field.length > 2)
                                    vertex.normal.set(normals.get(Integer.parseInt(field[2])-1));

                                group.vertices.add(vertex);
                            }

                        } else if(fields.length == 5) { // quad
                            Vertex[] face1 = new Vertex[3];
                            Vertex[] face2 = new Vertex[3];

                            for(int vi = 0; vi < 4; vi++) {
                                String[] field = fields[vi+1].split("/");
                                Vertex vertex = vertices.get(Integer.parseInt(field[0])-1);

                                if(field.length > 1)
                                    vertex.texcoord.set(uvcoords.get(Integer.parseInt(field[1])-1));

                                if(field.length > 2)
                                    vertex.normal.set(normals.get(Integer.parseInt(field[2])-1));

                                if(vi == 0) {
                                    face1[0] = vertex;
                                    face2[0] = vertex;
                                } else if(vi == 1) {
                                    face1[1] = vertex;
                                } else if(vi == 2) {
                                    face1[2] = vertex;
                                    face2[1] = vertex;
                                } else {
                                    face2[2] = vertex;
                                }                                 
                            }

                            group.vertices.add(face1[0]);
                            group.vertices.add(face1[1]);
                            group.vertices.add(face1[2]);
                            
                            group.vertices.add(face2[0]);
                            group.vertices.add(face2[1]);
                            group.vertices.add(face2[2]);

                        }
                    }    
                }
            }
        } finally {
            if(reader != null) {
                try { reader.close(); }
                catch(Exception ex) { ex.printStackTrace(System.err); }
            }
        }
        
        return model;
    }
}
