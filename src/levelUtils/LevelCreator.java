package levelUtils;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import utilities.Globals;
import gameLogic.Entity;
import graphics.Graphics3D;

public class LevelCreator {

	public LevelCreator(){
		AssetManager am = new AssetManager();
		am.setVisible(true);
		/*
		Graphics3D g = new Graphics3D();
		JFileChooser levelChooser = new JFileChooser();
		levelChooser.setCurrentDirectory(new File("res/maps/").getAbsoluteFile());
		int action = levelChooser.showOpenDialog(null);
		if(action == JFileChooser.APPROVE_OPTION){
			try {
				levelChooser.getSelectedFile().createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Globals.activeLevel = new Level(levelChooser.getSelectedFile().getAbsolutePath());
			Entity charlie = Entity.loadEntity("res/assets/Characters/Charlie.entity","res/assets/Characters/Charlie.asset");
			charlie.getAsset().putModel("res/meshes/Characters/Charlie.obj", "res/meshes/Characters/Charlie.mtl");
			charlie.saveEntity();
			Globals.entities.add(charlie);
			
			g.getCamera().setEntityCameraLock(charlie);
			g.start();
			Globals.activeLevel.saveLevel();
		}
		*/
	}
	
	public static void main(String args[]){
		new LevelCreator();
	}
}
