package LevelUtils;

import java.io.File;

import javax.swing.JFileChooser;

import GameLogic.Entity;
import utilities.Globals;
import graphics.Graphics3D;

public class LevelCreator {

	public LevelCreator(){
		Graphics3D g = new Graphics3D();
		JFileChooser levelChooser = new JFileChooser();
		levelChooser.setCurrentDirectory(new File("res/maps/").getAbsoluteFile());
		int action = levelChooser.showOpenDialog(null);
		if(action == JFileChooser.APPROVE_OPTION)
			System.out.println(levelChooser.getSelectedFile());
		Globals.activeLevel = new Level("res/maps/map1.map");
		Entity charlie = Entity.loadEntity("res/assets/Characters/Charlie.entity","res/assets/Characters/Charlie.asset");
		charlie.getAsset().putModel("res/meshes/Characters/Charlie.obj", "res/meshes/Characters/Charlie.mtl");
		charlie.saveEntity();
		Globals.entities.add(charlie);
		
		g.getCamera().setEntityCameraLock(charlie);
		g.start();
		Globals.activeLevel.saveLevel();
	}
	
	public static void main(String args[]){
		new LevelCreator();
	}
}
