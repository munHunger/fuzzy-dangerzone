package gameLogic;

import java.io.Serializable;

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
		Entity charlie = Entity.loadEntity("res/assets/Characters/Charlie.entity","res/assets/Characters/Charlie.asset");
		charlie.getAsset().putModel("res/meshes/Characters/Charlie.obj", "res/meshes/Characters/Charlie.mtl");
		charlie.saveEntity();
		Globals.entities.add(charlie);
		
		Entity npcCharlie = Entity.loadEntity("res/assets/Characters/Charlie.entity","res/assets/Characters/Charlie.asset");
		npcCharlie.setPos(10, 15);
		Globals.entities.add(npcCharlie);
		
		Asset barrel = Asset.loadAsset("res/assets/Misc/Barrel.asset");
		barrel.setWalkable(false);
		barrel.saveAsset();
		Asset sand = Asset.loadAsset("res/assets/GroundTiles/Sand.asset");
		sand.putModel("res/meshes/GroundTiles/Sand.obj", "res/meshes/GroundTiles/Sand.mtl");
		sand.setWalkable(true);
		sand.saveAsset();
		Asset horizontalRailing = Asset.loadAsset("res/assets/Walls/HorizontalRailing.asset");
		horizontalRailing.putModel("res/meshes/Walls/Railing.obj", "res/meshes/Walls/Railing.mtl");
		horizontalRailing.setWalkable(false);
		horizontalRailing.saveAsset();
		Asset verticalRailing = Asset.loadAsset("res/assets/Walls/VerticalRailing.asset");
		verticalRailing.putModel("res/meshes/Walls/Railing.obj", "res/meshes/Walls/Railing.mtl");
		verticalRailing.setWalkable(false);
		verticalRailing.setzRot(90f);
		verticalRailing.saveAsset();
		
		Asset pillar = Asset.loadAsset("res/assets/Walls/Pillar.asset");
		pillar.putModel("res/meshes/Walls/Pillar.obj", "res/meshes/Walls/Pillar.mtl");
		pillar.setWalkable(false);
		pillar.saveAsset();
		
		Asset stoneStair = Asset.loadAsset("res/assets/Misc/StoneStair.asset");
		stoneStair.putModel("res/meshes/Misc/StoneStair.obj", "res/meshes/Misc/StoneStair.mtl");
		stoneStair.setWalkable(true);
		stoneStair.setzRot(90);
		stoneStair.saveAsset();
		
		for(int x = 0; x < Globals.activeLevel.getWidth(); x++)
			for(int y = 0; y < Globals.activeLevel.getHeight(); y++){
				Globals.activeLevel.addAsset(x, y, 0, sand);
			}
		for(int x = 1; x < Globals.activeLevel.getWidth()-1; x++){
			Globals.activeLevel.addAsset(x, 0, 0, horizontalRailing);
			Globals.activeLevel.addAsset(x, Globals.activeLevel.getHeight()-1, 0, horizontalRailing);
		}
		
		for(int y = 1; y < Globals.activeLevel.getWidth()-1; y++){
			Globals.activeLevel.addAsset(0, y, 0, verticalRailing);
			Globals.activeLevel.addAsset(Globals.activeLevel.getWidth()-1, y, 0, verticalRailing);
		}

		Globals.activeLevel.addAsset(0, 0, 0, pillar);
		Globals.activeLevel.addAsset(Globals.activeLevel.getWidth()-1, 0, 0, pillar);
		Globals.activeLevel.addAsset(0, Globals.activeLevel.getHeight()-1, 0, pillar);
		Globals.activeLevel.addAsset(Globals.activeLevel.getWidth()-1, Globals.activeLevel.getHeight()-1, 0, pillar);

		Globals.activeLevel.addAsset(3, 2, 0, barrel);
		Globals.activeLevel.addAsset(3, 3, 1, barrel);
		Globals.activeLevel.addAsset(3, 4, 1, barrel);
		Globals.activeLevel.addAsset(3, 5, 0, barrel);
		Globals.activeLevel.addAsset(4, 2, 0, stoneStair);
		Globals.activeLevel.saveLevel();
		g.getCamera().setEntityCameraLock(charlie);
		g.start();
	}
	
	public static void main(String args[]){
		new Main();
	}
}
