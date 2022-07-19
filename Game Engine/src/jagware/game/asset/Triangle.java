/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

/**
 *
 * @author jacob
 */
public class Triangle {
    
    public final Vertex[] vertices;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.vertices = new Vertex[] { v1, v2, v3 };
    }
}
