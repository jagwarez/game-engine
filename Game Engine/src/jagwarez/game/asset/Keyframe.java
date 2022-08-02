/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagwarez.game.asset;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author jacob
 */
public class Keyframe {
    
    public final float time;
    public final Matrix4f transform;
    public final Vector3f position;
    public final Quaternionf rotation;
    
    public Keyframe(float time, Matrix4f transform) {
        this.time = time;
        this.transform = transform;
        this.position = transform.getTranslation(new Vector3f());
        this.rotation = transform.getNormalizedRotation(new Quaternionf());
    }
}
