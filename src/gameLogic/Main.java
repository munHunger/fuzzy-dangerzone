package gameLogic;

import levelUtils.Level;
import utilities.Globals;
import graphics.Graphics3D;

public class Main {
	public Main(){
		Graphics3D g = new Graphics3D();
		Globals.activeLevel = Level.loadLevel("res/maps/levelCreator.map");
		g.start(null);
	}
	
	public static void main(String args[]){
		new Main();
	}
}
