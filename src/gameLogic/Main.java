package gameLogic;

import java.util.ArrayList;

import levelUtils.AssetFunctions;
import levelUtils.Level;
import utilities.Globals;
import graphics.Graphics3D;

public class Main {
	public Main(){
		Graphics3D g = new Graphics3D();
		Globals.activeLevel = Level.loadLevel("res/maps/levelCreator.map");
		AssetFunctions.initFunctionMap();
		
		ArrayList<String> gordonAssetNames = new ArrayList<>();
		gordonAssetNames.add("res/assets/Characters/Gordon/BipedLegs.asset");
		gordonAssetNames.add("res/assets/Characters/Gordon/Cockpit.asset");
		gordonAssetNames.add("res/assets/Characters/Gordon/Gun.asset");
		gordonAssetNames.add("res/assets/Characters/Gordon/Sheep.asset");
		gordonAssetNames.add("res/assets/Characters/Gordon/TopHat.asset");
		Entity gordon = Entity.loadEntity("res/assets/Characters/Gordon/Gordon.entity", gordonAssetNames);
		gordon.saveEntity();
		gordon.setPos(10, 10);
		g.debug = true;
		Globals.entities.add(gordon);
		g.getCamera().setEntityCameraLock(gordon);
		g.start(null);
	}
	
	public static void main(String args[]){
		new Main();
	}
}
