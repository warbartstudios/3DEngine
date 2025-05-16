package org.engineTester;

import org.entity.Camera;
import org.entity.Entity;
import org.entity.Light;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import static org.lwjgl.opengl.Display.*;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.vector.Vector3f;
import org.models.RawModel;
import org.models.TexturedModel;
import org.renderEngine.Loader;
import org.renderEngine.MasterRenderer;
import org.renderEngine.OBJLoader;
import org.terrains.Terrain;
import org.textures.ModelTexture;

import tools.FontTT;
import tools.Texture;

public class MainGameLoop {

	static FontTT myFont;
	static Texture myTexture;
	
    private static void initDisplay() {
    	PixelFormat pixelFormat = new PixelFormat();
        ContextAttribs attribs = new ContextAttribs(3,2)
        .withForwardCompatible(true)
        .withProfileCore(true);
        
        try {
            setDisplayMode(new DisplayMode(640,480));
            create(pixelFormat, attribs);
            setTitle("JiferEngine");
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
        
        glViewport(0, 0, 640, 480);
        
        
    }
    
    public static void main(String[] args){

    	initDisplay();
    	
        Loader loader = new Loader();
        Loader loader2 = new Loader();
        Loader loader3 = new Loader();
        
        RawModel model = OBJLoader.loadObjModel("bunny", loader);
        RawModel model2 = OBJLoader.loadObjModel("lowPolyTree", loader2);
        RawModel model3 = OBJLoader.loadObjModel("dragon", loader3);
        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("red")));
        TexturedModel staticModel2 = new TexturedModel(model2, new ModelTexture(loader.loadTexture("lowPolyTree")));
        TexturedModel staticModel3 = new TexturedModel(model3, new ModelTexture(loader.loadTexture("blue")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader), new ModelTexture(loader.loadTexture("grassTexture")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader), new ModelTexture(loader.loadTexture("fern")));
        fern.getTexture().setHasTransparency(true);
        fern.getTexture().setUseFakeLighting(true);
        
        Entity entity = new Entity(staticModel, new Vector3f(0, 0, 25), 0, 0, 0, 1);
        Entity entity2 = new Entity(staticModel2, new Vector3f(0, 0, -25), 0, 0, 0, 1);
        Entity entity3 = new Entity(staticModel3, new Vector3f(0, 0, 0), 0, 0, 0, 1);
        Light light = new Light(new Vector3f(3000,2000,3000), new Vector3f(1,1,1));
        
        Terrain terrain = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(1, 0, loader, new ModelTexture(loader.loadTexture("grass")));
        
        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();
        
        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
        	entities.add(new Entity(staticModel2, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * - 600), 0, 0, 0, 1));
        	entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * - 600), 0, 0, 0, 3));
        	entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * - 600), 0, 0, 0, 0.6f));
        }
        
        while(!isCloseRequested()) {
        	entity3.increaseRotation(0, 1, 0);
        	entity2.increaseRotation(0, 1, 0);
        	entity.increaseRotation(0, 1, 0);
            camera.move();
            
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processEntity(entity);
            renderer.processEntity(entity2);
            renderer.processEntity(entity3);
            for (Entity entit : entities) {
            	renderer.processEntity(entit);
            }
            renderer.render(light, camera);
            sync(60);
    		update();
            
        }

        renderer.cleanUp();
        loader.cleanUp();
        destroy();
        
    }
    
}
