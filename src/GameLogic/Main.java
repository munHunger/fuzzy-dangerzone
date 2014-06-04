package GameLogic;

import utilities.Globals;
import utilities.Invoke;
import LevelUtils.Asset;
import LevelUtils.Level;
import graphics.Graphics3D;

public class Main {
	public Main(){
		Graphics3D g = new Graphics3D();
		Globals.activeLevel = new Level("res/maps/map1.map");
		Entity charlie = Entity.loadEntity("res/assets/Characters/Charlie.entity","res/assets/Characters/Charlie.asset");
		charlie.getAsset().putModel("res/meshes/Characters/Charlie.obj", "res/meshes/Characters/Charlie.mtl");
		charlie.saveEntity();
		Globals.entities.add(charlie);
		
		Asset barrel = Asset.loadAsset("res/assets/Misc/Barrel.asset");
		barrel.setWalkable(false);
		barrel.saveAsset();
		Asset sand = Asset.loadAsset("res/assets/GroundTiles/Sand.asset");
		sand.setWalkable(true);
		sand.onWalkOn = new Invoke<Boolean, Entity>(){
				public Boolean call(Entity e) {
					System.out.println(e.getxPos());
					return true;
				}
			};
		sand.saveAsset();
		for(int x = 0; x < Globals.activeLevel.getWidth(); x++)
			for(int y = 0; y < Globals.activeLevel.getHeight(); y++){
				Globals.activeLevel.addAsset(x, y, 0, sand);
				if(x == 0 || y == 0 || x == Globals.activeLevel.getWidth()-1 || y == Globals.activeLevel.getHeight()-1 || x == y)
					Globals.activeLevel.addAsset(x, y, 0, barrel);
			}
		Globals.activeLevel.addAsset(1, 2, 0, barrel);
		Globals.activeLevel.saveLevel();
		g.getCamera().setEntityCameraLock(charlie);
		g.start();
	}
	
	public static void main(String args[]){
		new Main();
	}
}
