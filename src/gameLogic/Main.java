package gameLogic;

import java.io.Serializable;
import java.util.ArrayList;

import levelUtils.Asset;
import levelUtils.Level;
import utilities.Globals;
import utilities.Invoke;
import utilities.InvokeWrapper;
import graphics.Graphics3D;

public class Main {
	public Main(){
		Graphics3D g = new Graphics3D();
		Globals.activeLevel = new Level("res/maps/map1.map");
		
		ArrayList<String> gordonAssetNames = new ArrayList<>();
		gordonAssetNames.add("res/assets/Characters/Gordon/BipedLegs.asset");
		gordonAssetNames.add("res/assets/Characters/Gordon/Cockpit.asset");
		gordonAssetNames.add("res/assets/Characters/Gordon/Gun.asset");
		gordonAssetNames.add("res/assets/Characters/Gordon/Sheep.asset");
		gordonAssetNames.add("res/assets/Characters/Gordon/TopHat.asset");
		Entity gordon = Entity.loadEntity("res/assets/Characters/Gordon/Gordon.entity", gordonAssetNames);
		gordon.saveEntity();
		
		Entity npcGordon = Entity.loadEntity("res/assets/Characters/Gordon/Gordon.entity", gordonAssetNames);
		npcGordon.setPos(10, 15);
		Globals.entities.add(npcGordon);
		Globals.entities.add(gordon);
		g.getCamera().setEntityCameraLock(gordon);
		
		Asset barrel = Asset.loadAsset("res/assets/Misc/Barrel.asset");
		Asset sand = Asset.loadAsset("res/assets/GroundTiles/Sand.asset");
		
		for(int x = 0; x < Globals.activeLevel.getWidth(); x++)
			for(int y = 0; y < Globals.activeLevel.getHeight(); y++)
				Globals.activeLevel.addAsset(x, y, 0, sand);

		Globals.activeLevel.addAsset(3, 2, 0, barrel);
		Globals.activeLevel.addAsset(3, 3, 1, barrel);
		Globals.activeLevel.addAsset(3, 4, 1, barrel);
		Globals.activeLevel.addAsset(3, 5, 0, barrel);
		Globals.activeLevel.saveLevel();
		g.start();
	}
	
	public static void main(String args[]){
		new Main();
	}
}
