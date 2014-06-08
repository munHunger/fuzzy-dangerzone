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

import utilities.Globals;

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
	private ArrayList<Asset>[][][] mapAssets;
	private String levelLocation;
	
	
	@SuppressWarnings("unchecked")
	public Level(String fileLocation, int width, int height, int depth){
		this.levelLocation = fileLocation;
		this.width = width;
		this.height = height;
		this.setDepth(depth);
		mapAssets = new ArrayList[width][height][depth];
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				for(int z = 0; z < depth; z++)
					mapAssets[x][y][z] = new ArrayList<>();
	}

	public Level(String fileLocation){
		this(fileLocation, 30, 30, 10);
	}
	
	public boolean isWalkable(int x, int y, int z){
		for(Asset a : mapAssets[x][y][z])
			if(!a.isWalkable())
				return false;
		for(Asset a : mapAssets[x+1][y][z])
			if(!a.isWalkable())
				return false;
		for(Asset a : mapAssets[x][y+1][z])
			if(!a.isWalkable())
				return false;
		for(Asset a : mapAssets[x+1][y+1][z])
			if(!a.isWalkable())
				return false;
		return true;
	}
	
	public ArrayList<Asset> getAssetList(int x, int y, int z){
		return mapAssets[x][y][z];
	}
	
	public void addAsset(int x, int y, int z, Asset a){
		mapAssets[x][y][z].add(a);
	}
	
	public void saveLevel(){
		File f = new File(levelLocation);
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
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				for(int z = 0; z < depth; z++)
					for(Asset a : getAssetList(x, y, z))
						a.setupAsset();
	}

	public void walkAction(int prevX, int prevY, int x, int y, int z, Entity e) {
		/*
		for(Asset a : mapAssets[x][y][z])
			if(a.onWalkOn != null)
				a.onWalkOn.call(e);
		for(Asset a : mapAssets[x+1][y][z])
			if(a.onWalkOn != null)
				a.onWalkOn.call(e);
		for(Asset a : mapAssets[x][y+1][z])
			if(a.onWalkOn != null)
				a.onWalkOn.call(e);
		for(Asset a : mapAssets[x+1][y+1][z])
			if(a.onWalkOn != null)
				a.onWalkOn.call(e);
		*/
	}
}
