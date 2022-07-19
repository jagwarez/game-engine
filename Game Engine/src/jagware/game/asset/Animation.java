/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jagware.game.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author jacob
 */
public class Animation {

    public final String name;
    public final Map<String,Channel> channels;
    private float duration = 0f;
    
    public Animation(String name) {
        this.name = name;
        this.channels = new HashMap<>();
    }
    
    public void animate(float time) {
        for(Channel channel : channels.values())
            channel.animate(time);
    }
}
