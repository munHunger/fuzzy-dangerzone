package gameLogic;

import java.util.ArrayList;

import levelUtils.Asset;
import levelUtils.Level;
import utilities.Globals;
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
		barrel.setPos(5, 5, 0);
		Globals.activeLevel.addAsset(barrel);
		
		Level level = Globals.activeLevel;
		g.start(null);
	}
	
	public static void main(String args[]){
		new Main();
	}
}
