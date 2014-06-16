package gameLogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import levelUtils.Asset;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import utilities.Globals;

public class Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6832619194322173349L;

	private float xPos;
	private float yPos;
	private float zPos;
	private float prevxPos;
	private float prevyPos;
	private float zRot;
	private float xLargeScale, yLargeScale, zLargeScale;
	private float xSmallScale, ySmallScale, zSmallScale;
	private ArrayList<Asset> assets = new ArrayList<>();

	private String entityName;
	public Entity(String entityName, ArrayList<String> assetNames) {
		this.entityName = entityName;
		for(String assetName : assetNames){
			Asset a = Asset.loadAsset(assetName);
			assets.add(a);
			a.setupAsset();
		}
		calculateBoundingBox();
	}
	
	public void calculateBoundingBox(){
		xLargeScale = 0f;
		yLargeScale = 0f;
		zLargeScale = 0f;
		xSmallScale = 0f;
		ySmallScale = 0f;
		zSmallScale = 0f;
		for(Asset a : assets){
			if(a.getXLargeScale() > xLargeScale)
				xLargeScale = a.getXLargeScale();
			if(a.getXSmallScale() < xSmallScale)
				xSmallScale = a.getXSmallScale();

			if(a.getYLargeScale() > yLargeScale)
				yLargeScale = a.getYLargeScale();
			if(a.getYSmallScale() < ySmallScale)
				ySmallScale = a.getYSmallScale();

			if(a.getZLargeScale() > zLargeScale)
				zLargeScale = a.getZLargeScale();
			if(a.getZSmallScale() < zSmallScale)
				zSmallScale = a.getZSmallScale();
		}
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
	public void setzRot(float rot){
		this.zRot = rot;
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
	
	public boolean setPos(float x, float y){
		if(!Globals.activeLevel.isWalkable(x+xSmallScale, y+ySmallScale, zPos+zSmallScale, Math.abs(xSmallScale-xLargeScale), Math.abs(ySmallScale-yLargeScale), Math.abs(zSmallScale-zLargeScale)))
			return false;
		prevxPos = xPos;
		prevyPos = yPos;
		xPos = x;
		yPos = y;
		Globals.activeLevel.walkAction(x+xSmallScale, y+ySmallScale, zPos+zSmallScale, Math.abs(xSmallScale-xLargeScale), Math.abs(ySmallScale-yLargeScale), Math.abs(zSmallScale-zLargeScale), this);
		if(prevxPos != xPos || prevyPos != yPos)
			zRot = (float) Math.toDegrees(Math.atan2(xPos-prevxPos, prevyPos-yPos));
		return true;
	}

	public boolean setPos(float speedModifier) {
		float x = this.xPos;
		float y = this.yPos;
		float z = this.zPos;
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			x -= speedModifier;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
			x += speedModifier;
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			y -= speedModifier;
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			y += speedModifier;

		if(!Globals.activeLevel.isWalkable(x+xSmallScale, y+ySmallScale, z+zSmallScale, Math.abs(xSmallScale-xLargeScale), Math.abs(ySmallScale-yLargeScale), Math.abs(zSmallScale-zLargeScale)))
			return false;
		Globals.activeLevel.walkAction(x+xSmallScale, y+ySmallScale, zPos+zSmallScale, Math.abs(xSmallScale-xLargeScale), Math.abs(ySmallScale-yLargeScale), Math.abs(zSmallScale-zLargeScale), this);

		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
		return true;
	}

	public ArrayList<Asset> getAssets() {
		return assets;
	}

	public void setAssets(ArrayList<Asset> assets) {
		this.assets = assets;
	}

	public static Entity loadEntity(String entityName, ArrayList<String> assetNames) {
		Entity entity = null;
		File f = new File(entityName);
		if(f.exists()){
			try {
				ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(f));
				Object o = inStream.readObject();
				if(o instanceof Entity){
					entity = (Entity)o;
					for(Asset a : entity.assets)
						a.setupAsset();
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
			entity = new Entity(entityName, assetNames);
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
