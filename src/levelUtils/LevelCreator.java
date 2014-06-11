package levelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.lwjgl.input.Keyboard;

import utilities.Globals;
import gameLogic.Entity;
import graphics.Graphics3D;

public class LevelCreator{
	
	private static Graphics3D g3d;
	private AssetManager am;
	private Entity cursor = new Entity("cursor", new ArrayList<String>());
	private File prevSelection = null;
	public LevelCreator(){
		AssetFunctions.initFunctionMap();
		am = new AssetManager();
		am.setVisible(true);
		Globals.activeLevel = Level.loadLevel("res/maps/levelCreator.map");
		Globals.entities.add(cursor);
		g3d = new Graphics3D();
		cursor.setPos(10, 15);
		g3d.getCamera().setEntityCameraLock(cursor);
		g3d.start(new Callable<Integer>() {
			public Integer call() throws Exception {
				step();
				return null;
			}
		});
	}
	
	public static void main(String args[]){
		new LevelCreator();
	}

	public void step() {
		if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
			if(cursor.getAssets().size() > 0){
				System.out.println("Placing asset");
				Asset a = cursor.getAssets().get(0);
				a.setPos(cursor.getxPos(), cursor.getyPos(), cursor.getzPos());
				Globals.activeLevel.addAsset(a);
				cursor.setAssets(new ArrayList<Asset>());
			}
		}
		File f = am.getSelectedAsset();
		if((f == null && prevSelection == null) || f.equals(prevSelection))
			return;
		prevSelection = f;
		if(f != null){
			ArrayList<Asset> cursorAssets = new ArrayList<>();
			cursorAssets.add(Asset.loadAsset(f.getAbsolutePath()));
			cursor.setAssets(cursorAssets);
		}
	}
}
