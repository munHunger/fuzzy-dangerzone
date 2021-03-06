package levelUtils;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import graphics.utilities.Face;
import graphics.utilities.Model;
import graphics.utilities.ModelPart;
import graphics.utilities.OBJLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.lwjgl.util.vector.Vector3f;


/**
 * Asset is an object in the game that have a visual property in form of a 3d Model.
 * It also have the ability to change the world if hit or walked upon.
 * @author Marcus
 *
 */
public class Asset implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5625127324145423388L;
	private int modelListHandle;
	private Model model;
	private String assetName;
	private float zRot;
	
	private float xPos = 0;
	private float yPos = 0;
	private float zPos = 0;
	private float xLargeScale, yLargeScale, zLargeScale;
	private float xSmallScale, ySmallScale, zSmallScale;
	
	private int health;
	public String onWalkOn;
	public String onWalkOff;
	public String onHit;
	private boolean walkable;

	public static Asset loadAsset(String assetName){
		Asset a = null;
		File f = new File(assetName);
		if(f.exists()){
			try {
				ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(f));
				Object o = inStream.readObject();
				if(o instanceof Asset){
					a = (Asset)o;
					if(a.model != null)
						a.setupModelList(a.model.getModelName(), a.model.getMtlPath());
				}
				inStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(a == null)
			a = new Asset(assetName);
		return a;
	}

	public void saveAsset(){
		File f = new File(assetName);
		try {
			f.createNewFile();
			ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(f));
			outStream.writeObject(this);
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Asset(String assetName){
		this.assetName = assetName;
		//modelListHandle = setupModelList(assetName + ".obj");
	}
	
	public void setPos(float x, float y, float z){
		xPos = x;
		yPos = y;
		zPos = z;
	}

	public float getX(){
		return xPos;
	}
	public float getY(){
		return yPos;
	}
	public float getZ(){
		return zPos;
	}
	public float getXLargeScale(){
		return xLargeScale;
	}
	public float getYLargeScale(){
		return yLargeScale;
	}
	public float getZLargeScale(){
		return zLargeScale;
	}
	public float getXSmallScale(){
		return xSmallScale;
	}
	public float getYSmallScale(){
		return ySmallScale;
	}
	public float getZSmallScale(){
		return zSmallScale;
	}
	
	public void calculateBoundingBox(){
		xLargeScale = 0f;
		yLargeScale = 0f;
		zLargeScale = 0f;
		xSmallScale = 0f;
		ySmallScale = 0f;
		zSmallScale = 0f;
		double zRot = (Math.toRadians(this.zRot));
		for(Vector3f v : model.getVerticies()){
			float r = (float) Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2));
			float vRot = (float) Math.atan2(v.y, v.x);
			float x = (float) (r * Math.cos(vRot+zRot));
			float y = (float) (r * Math.sin(vRot+zRot));
			float z = v.z;
			if(x > xLargeScale)
				xLargeScale = x;
			if(x < xSmallScale)
				xSmallScale = x;

			if(y > yLargeScale)
				yLargeScale = y;
			if(y < ySmallScale)
				ySmallScale = y;

			if(z > zLargeScale)
				zLargeScale = z;
			if(z < zSmallScale)
				zSmallScale = z;
		}
		
		
	}
	
	public void loadModel(String modelName, String mtlPath){
		try {
			model = OBJLoader.loadModel(new File(modelName), new File(mtlPath));
			model.setModelName(modelName);
			model.setMtlPath(mtlPath);
			calculateBoundingBox();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void putModel(String modelName, String mtlPath){
		setupModelList(modelName, mtlPath);
	}
	
	public int getModelListHandle() {
		return modelListHandle;
	}
	
	public Model getModel(){
		return model;
	}

	/**
	 * Loads a .obj file into a Model object and then converts the Model to a list that is renderable using openGL.
	 * @param modelName The name of the model. This should be the relative search path to an .obj file
	 * @return the integer handle to the openGL compiled list
	 * @see OBJLoader
	 */
	private void setupModelList(String modelName, String mtlPath) {
		modelListHandle = glGenLists(1);
		glNewList(modelListHandle, GL_COMPILE);
		{
			loadModel(modelName, mtlPath);
			for(ModelPart modelPart : model.getModelParts()){
				Vector3f color = modelPart.getColor();
				glColor3f(color.x, color.y, color.z);
				glBegin(GL_TRIANGLES);
				for (Face face : modelPart.getFaces()) {
					Vector3f n1 = model.getNormals().get((int)(face.getNormals().x - 1));
					glNormal3f(n1.x, n1.y, n1.z);
					Vector3f v1 = model.getVerticies().get((int)(face.getVerticies().x - 1));
					glVertex3f(v1.x, v1.y, v1.z);

					Vector3f n2 = model.getNormals().get((int)(face.getNormals().y - 1));
					glNormal3f(n2.x, n2.y, n2.z);
					Vector3f v2 = model.getVerticies().get((int)(face.getVerticies().y - 1));
					glVertex3f(v2.x, v2.y, v2.z);

					Vector3f n3 = model.getNormals().get((int)(face.getNormals().z - 1));
					glNormal3f(n3.x, n3.y, n3.z);
					Vector3f v3 = model.getVerticies().get((int)(face.getVerticies().z - 1));
					glVertex3f(v3.x, v3.y, v3.z);
				}
				glEnd();
			}
		}
		glEndList();
	}

	public void setupAsset() {
		if(model != null)
			setupModelList(model.getModelName(), model.getMtlPath());
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isWalkable() {
		return walkable;
	}

	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}

	public float getzRot() {
		return zRot;
	}

	public void setzRot(float zRot) {
		this.zRot = zRot;
	}
}
