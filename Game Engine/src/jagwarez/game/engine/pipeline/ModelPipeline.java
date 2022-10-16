package jagwarez.game.engine.pipeline;

import jagwarez.game.asset.model.Color;
import jagwarez.game.asset.model.Effect;
import jagwarez.game.asset.model.Mesh;
import jagwarez.game.asset.model.Texture;
import jagwarez.game.engine.Entity;
import jagwarez.game.engine.Program;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 *
 * @author jacob
 */
public abstract class ModelPipeline extends RenderPipeline {
    
    protected void render(Entity entity) {
        render(entity, program);
    }
    
    protected void render(Entity entity, Program program) {
        
        program.uniform("identity").integer(entity.id);
        program.uniform("transform").matrix(entity);
        
        for(Mesh mesh : entity.model.meshes.values()) {

            for(Mesh.Group group : mesh.groups) {
                 
                Effect diffuse = group.material.effects.get(Effect.Parameter.DIFFUSE);
                
                if(diffuse != null) {
                    if(diffuse.type == Effect.Type.TEXTURE) {
                        Texture diffuseMap = (Texture) diffuse;

                        glActiveTexture(GL_TEXTURE0 + 0);
                        glBindTexture(GL_TEXTURE_2D, diffuseMap.id);
                        
                        program.uniform("useDiffuseMap").bool(true);
                        program.uniform("diffuseMap").integer(0);
                        
                    } else if(diffuse.type == Effect.Type.COLOR) {
                        program.uniform("diffuseColor").rgba((Color)diffuse);
                    }
                } else
                    program.uniform("diffuseColor").vector(0f, 0f, 0f, 1f);
           
                glDrawArrays(GL_TRIANGLES, group.offset, group.vertices.size());
                //glDrawElements(GL_TRIANGLES, group.indices.size(), GL_UNSIGNED_INT, group.offset);
                
                glBindTexture(GL_TEXTURE_2D, 0);
                
            }
        }
    }
}
