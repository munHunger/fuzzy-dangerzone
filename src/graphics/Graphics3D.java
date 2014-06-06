package graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import levelUtils.Asset;
import levelUtils.Level;
import gameLogic.Entity;
import graphics.utilities.Camera;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import utilities.Globals;

/**
 * Graphics viewer
 * @author Marcus
 * @version 1.0
 */
public class Graphics3D {
	private Camera camera;
	private float size = 1.0f;

	/**
	 * This is all that is needed.<br />
	 * Everything is dependant on Globals, so make sure to setup Globals before creating Graphics3D object or it will not work.<br />
	 * Press F to toggle fullscreen <br />
	 * Press escape to exit the application.<br />
	 * Note: Only use escape to exit!
	 */
	public Graphics3D(){
		setupDisplay();
		setupCamera();
		setupStates();
		setupLighting();
	}
	
	public void start(){
		glMatrixMode(GL_MODELVIEW);
		long lastTime = 0;
		updateLight(GL_LIGHT1, camera.getPosition(), new Vector3f(0.3f, 0.35f, 0.45f));
		while(!Display.isCloseRequested()){
			processInput();
			long time = System.currentTimeMillis();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glPushAttrib(GL_TRANSFORM_BIT);
			glPushMatrix();
			glLoadIdentity();
			camera.applyPerspective();
			camera.processKeyboardInput(lastTime*0.01f);
			camera.passMouseInput();
			camera.applyTranslations();
			
			render();

			glPopAttrib();

			lastTime = System.currentTimeMillis() - time;
			glPopMatrix();
			Display.update();
		}
	}

	/**
	 * Handles keyboard input related to the main graphic part.
	 * so, key 'f' to toggle fullscreen, and escape to exit the program
	 */
	private void processInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			try {
				Display.setFullscreen(!Display.isFullscreen());
				Thread.sleep(100);
			} catch (LWJGLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			Display.destroy();
			System.exit(0);
		}
	}

	/**
	 * Main render function.
	 * This renders animations, setups lights and calls other sub functions to render out everything to the screen.
	 */
	private void render() {
		Level level = Globals.activeLevel;
		for(int x = 0; x < level.getWidth(); x++)
			for(int y = 0; y < level.getHeight(); y++)
				for(int z = 0; z < level.getDepth(); z++)
					for(Asset a: level.getAssetList(x, y, z))
						renderModel(a.getModelListHandle(), new Vector3f(x*size, y*size, z*size), new Vector3f(0.0f, 0.0f, a.getzRot()), new Vector3f(1.0f, 1.0f, 1.0f));
		for(Entity e : Globals.entities)
			renderModel(e.getAsset().getModelListHandle(), new Vector3f(e.getxPos(), e.getyPos(), e.getzPos()), e.getRotation(), new Vector3f(1.0f, 1.0f, 1.0f));
	}

	/**
	 * Updates the position and color of a light
	 * @param light the light to update, this should be GL_LIGHTx, where x=0-9
	 * @param position a vector that points to the position of the light after movement
	 * @param color a vector that holds color information of the light
	 */
	private void updateLight(int light, Vector3f position, Vector3f color) {
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		glLight(light, GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{position.x, position.y, position.z, 1.0f}).flip());
		glLight(light, GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(new float[]{color.x, color.y, color.z, 1.0f}).flip());
		glPopMatrix();
	}

	private HashMap<Integer, FloatBuffer> lightScale = new HashMap<>();
	/**
	 * Scales the light intensity
	 * @param lights all lights to be scaled each of these integers should be GL_LIGHTx where x = 0-9
	 * @param scale the amount to scale
	 */
	private void scaleLights(int[] lights, float scale) {
		glMatrixMode(GL_MODELVIEW);
		glPushMatrix();
		for(int l = 0; l < lights.length; l++){
			if(!lightScale.containsKey(l)){
				ByteBuffer temp = ByteBuffer.allocateDirect(16);
				temp.order(ByteOrder.nativeOrder());
				FloatBuffer fBuffer = temp.asFloatBuffer();
				lightScale.put(l, fBuffer);
				glGetLight(lights[l], GL_DIFFUSE, fBuffer);
			}
			FloatBuffer fBuffer = lightScale.get(l);
			for(int i = 0; i < fBuffer.capacity(); i++){
				float f = fBuffer.get(i);
				f *= scale;
				fBuffer.put(i, f);
			}
			glLight(lights[l], GL_DIFFUSE, fBuffer);
		}
		glPopMatrix();
	}
	
	/**
	 * Renders the model stored in the openGL list in listHandle
	 * @param listHandle the openGL list handle to the model
	 * @param position The position of where to render the model
	 * @param rotation The rotation of the model.
	 * @param size The scale of the model. Do note that having any part of this vector set to 0 will "implode" the world.
	 */
	private void renderModel(int listHandle, Vector3f position, Vector3f rotation, Vector3f size) {
		glTranslatef(position.x, position.y, position.z);
		glRotatef(rotation.x, 1.0f, 0.0f, 0.0f);
		glRotatef(rotation.y, 0.0f, 1.0f, 0.0f);
		glRotatef(rotation.z, 0.0f, 0.0f, 1.0f);
		float lightScale = Math.min(size.x, Math.min(size.y, size.z));
		if(lightScale != 1.0f)
			scaleLights(new int[]{GL_LIGHT0, GL_LIGHT1}, lightScale);
		glScalef(size.x, size.y, size.z);
		glCallList(listHandle);
		glScalef(1.0f/size.x, 1.0f/size.y, 1.0f/size.z);
		if(lightScale != 1.0f)
			scaleLights(new int[]{GL_LIGHT0, GL_LIGHT1}, 1.0f/lightScale);
		glRotatef(-rotation.z, 0.0f, 0.0f, 1.0f);
		glRotatef(-rotation.y, 0.0f, 1.0f, 0.0f);
		glRotatef(-rotation.x, 1.0f, 0.0f, 0.0f);
		glTranslatef(-position.x, -position.y, -position.z);
	}

	/**
	 * Initial setup of all lighting.
	 * This will setup ambient light.
	 * start a few light sources and setup their color, position, cutoff and attenuation.
	 * it will also enable the light sources
	 */
	private void setupLighting() {
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		glLightModel(GL_LIGHT_MODEL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
		glLight(GL_LIGHT0, GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
		glLight(GL_LIGHT0, GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(new float[]{1.0f, 1.0f, 1.0f, 1.0f}).flip());
		glLight(GL_LIGHT0, GL_SPOT_DIRECTION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
		glLight(GL_LIGHT0, GL_SPOT_EXPONENT, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0f, 0.0f, 0.0f, 1.0f}).flip());
		glLight(GL_LIGHT1, GL_QUADRATIC_ATTENUATION, (FloatBuffer)temp.asFloatBuffer().put(new float[]{0.0001f, 0.0001f, 0.0001f, 1.0f}).flip());
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_LIGHT1);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}

	/**
	 * Sets up global openGL states.
	 * Mostly stuff like enable GL_DEPTH_TEST
	 */
	private void setupStates() {
		glEnable(GL_DEPTH_TEST);
		glShadeModel(GL_SMOOTH); //should be set to smooth by default but just in case.
	}

	/**
	 * Sets up a camera with perspective.
	 */
	private void setupCamera() {
		camera = new Camera(new Vector3f(-10.0f, -3.0f, -10.0f), new Vector3f(0.0f, 0.0f, -90.0f), 0.01f, 1000.0f);
		camera.applyPerspective();
	}

	public Camera getCamera(){
		return camera;
	}
	
	/**
	 * Opens the window where everything will be contained.
	 */
	private void setupDisplay() {
		try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			for(int i = 0; i < modes.length; i++){
				if(modes[i].getWidth() == Globals.screenWidth && modes[i].getHeight() == Globals.screenHeight && modes[i].isFullscreenCapable())
					Display.setDisplayMode(modes[i]);
			}

			Display.setTitle("TOPPER");

			//ByteBuffer[] iconList = new ByteBuffer[2];
			//iconList[0] = loadIcon("res/ICON16.png");
			//iconList[1] = loadIcon("res/ICON32.png");
			//Display.setIcon(iconList);

			Display.create();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * loads a taskbar/window icon to be used for the display.
	 * @param filename The location of the file to load
	 * @return ByteBuffer of the image
	 * @throws IOException
	 */
	private ByteBuffer loadIcon(String filename) throws IOException {
		BufferedImage image = ImageIO.read(new File(filename)); // load image
		// convert image to byte array
		byte[] buffer = new byte[image.getWidth() * image.getHeight() * 4];
		int counter = 0;
		for (int x = 0; x < image.getHeight(); x++)
			for (int y = 0; y < image.getWidth(); y++)
			{
				int colorSpace = image.getRGB(y, x);
				buffer[counter + 0] = (byte) ((colorSpace << 8) >> 24);
				buffer[counter + 1] = (byte) ((colorSpace << 16) >> 24);
				buffer[counter + 2] = (byte) ((colorSpace << 24) >> 24);
				buffer[counter + 3] = (byte) (colorSpace >> 24);
				counter += 4;
			}
		return ByteBuffer.wrap(buffer);
	}
}
