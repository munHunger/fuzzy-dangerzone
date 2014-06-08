package levelUtils;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import utilities.Globals;
import gameLogic.Entity;
import graphics.Graphics3D;

public class LevelCreator {

	public LevelCreator(){
		AssetFunctions.initFunctionMap();
		Graphics3D g = new Graphics3D();
		AssetManager am = new AssetManager();
		am.setVisible(true);
	}
	
	public static void main(String args[]){
		new LevelCreator();
	}
}
