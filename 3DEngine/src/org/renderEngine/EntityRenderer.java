package org.renderEngine;

import java.util.List;
import java.util.Map;

import org.entity.Entity;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.util.vector.Matrix4f;
import org.models.RawModel;
import org.models.TexturedModel;
import org.shaders.StaticShader;
import org.textures.ModelTexture;
import org.toolbox.Maths;

public class EntityRenderer {
    

	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		
	}
	
	
	
	public void render(Map<TexturedModel,List<Entity>> entities) {
		for (TexturedModel model:entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity:batch) {
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        ModelTexture texture = model.getTexture();
        if (texture.isHasTransparency()) {
        	MasterRenderer.disableCulling();
        }
        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
	}
    

}
