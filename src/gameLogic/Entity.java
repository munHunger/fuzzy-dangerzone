package gameLogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import levelUtils.Asset;

import org.lwjgl.util.vector.Vector3f;

import utilities.Globals;

public class Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6832619194322173349L;

	private float xPos;
	private float yPos;
	private float prevxPos;
	private float prevyPos;
	private float zRot;
	private float zPos;
	private Asset asset;

	private String entityName;
	public Entity(String entityName, String assetName) {
		this.entityName = entityName;
		asset = new Asset(assetName);
	}
	
	public float getxPos() {
		return xPos;
	}
	public void setxPos(float xPos) {
		this.xPos = xPos;
	}
	public float getyPos() {
		return yPos;
	}
	public void setyPos(float yPos) {
		this.yPos = yPos;
	}
	public float getzPos() {
		return zPos;
	}
	public void setzPos(float zPos) {
		this.zPos = zPos;
	}
	public boolean setPos(float x, float y){
		if(!Globals.activeLevel.isWalkable((int)x, (int)y, (int)zPos))
			return false;
		prevxPos = xPos;
		prevyPos = yPos;
		xPos = x;
		yPos = y;
		Globals.activeLevel.walkAction((int)prevxPos, (int)prevyPos, (int)x, (int)y, (int)zPos, this);
		if(prevxPos != xPos || prevyPos != yPos)
			zRot = (float) Math.toDegrees(Math.atan2(xPos-prevxPos, prevyPos-yPos));
		return true;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public static Entity loadEntity(String entityName, String assetName) {
		Entity entity = null;
		File f = new File(entityName);
		if(f.exists()){
			try {
				ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(f));
				Object o = inStream.readObject();
				if(o instanceof Entity){
					entity = (Entity)o;
					entity.asset.setupAsset();
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
		if(entity == null)
			entity = new Entity(entityName, assetName);
		return entity;
	}

	public void saveEntity() {
		File f = new File(entityName);
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

	public Vector3f getRotation() {
		return new Vector3f(0.0f, 0.0f, this.zRot);
	}

	public void action(boolean leftButtonDown, boolean rightButtonDown, int x, int y) {
		if(leftButtonDown){
			zRot = (float) Math.toDegrees(Math.atan2(x-Globals.screenWidth/2, Globals.screenHeight/2-y))+90;
		}
	}

}
