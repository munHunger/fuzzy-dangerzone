package levelUtils;

import gameLogic.Entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utilities.Globals;
import utilities.Octree;

/**
 * A map is a 3D grid level where each cell in the grid got a modelList and a boolean for walkable.
 * Contains all model list handles for the level.
 * Also contains a boolean array for walkable positions.
 * @author Marcus
 * @version 1.0
 */
public class Level implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4414014626771600253L;
	private int height;
	private int width;
	private int depth;
	private Octree<Asset> mapAssets;
	private String levelLocation;
	
	
	public Level(String fileLocation, int width, int height, int depth){
		this.levelLocation = fileLocation;
		this.width = width;
		this.height = height;
		this.setDepth(depth);
		mapAssets = new Octree<>(width, height, depth);
	}

	public Level(String fileLocation){
		this(fileLocation, 30, 30, 10);
	}
	
	public boolean isWalkable(float x, float y, float z, float xScale, float yScale, float zScale){
		List<Asset> l = mapAssets.query(x, y, z, xScale, yScale, zScale);
		if(l == null)
			return false;
		for(Asset a : l)
			if(!a.isWalkable())
				return false;
		return true;
	}

	public void walkAction(float x, float y, float z, float xScale, float yScale, float zScale, Entity e) {
		List<Asset> l = mapAssets.query(x, y, z, xScale, yScale, zScale);
		if(l != null){
			for(Asset a : l){
				AssetFunctions.callFunction(a.onWalkOn, e);
			}
		}
	}
	
	public ArrayList<Asset> getAssetList(float x, float y, float z, float xScale, float yScale, float zScale){
		return (ArrayList<Asset>) mapAssets.query(x, y, z, xScale, yScale, zScale);
	}
	
	public void addAsset(Asset a){
		mapAssets.insert(a, a.getX()+a.getXSmallScale(), a.getY()+a.getYSmallScale(), a.getZ()+a.getZSmallScale(), 
				a.getXLargeScale()-a.getXSmallScale(), a.getYLargeScale()-a.getYSmallScale(), a.getZLargeScale()-a.getZSmallScale());
	}
	
	public void saveLevel(){
		File f = new File(levelLocation);
		try {
			f.createNewFile();
			ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(f));
			outStream.writeObject(this);
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public static Level loadLevel(String levelName) {
		Level l = null;
		File f = new File(levelName);
		if(f.exists()){
			try {
				ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(f));
				Object o = inStream.readObject();
				if(o instanceof Level){
					l = (Level)o;
					l.loadAssets();
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
		if(l == null)
			l = new Level(levelName);
		return l;
	}

	private void loadAssets() {
		for(Asset a : getAssetList(0, 0, 0, width-0.0001f, height-0.0001f, depth-0.0001f))
			a.setupAsset();
	}

	public void setLevelLocation(String levelLocation) {
		this.levelLocation = levelLocation;
	}
}
