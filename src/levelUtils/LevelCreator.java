package levelUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.input.Keyboard;

import utilities.Globals;
import gameLogic.Entity;
import graphics.Graphics3D;

public class LevelCreator{
	
	private static Graphics3D g3d;
	private AssetManager am;
	private Entity cursor = new Entity("cursor", new ArrayList<String>()){

		private static final long serialVersionUID = 1L;
		
		boolean snap = false;
		public void action(boolean leftButtonDown, boolean rightButtonDown, int x, int y) {
			if(leftButtonDown)
				setzRot(0);
		}
		
		public boolean setPos(float speedModifier) {
			float x = this.getxPos();
			float y = this.getyPos();
			float z = this.getzPos();
			if(Keyboard.isKeyDown(Keyboard.KEY_R)){
				g3d.debug = !g3d.debug;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
				snap = !snap;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_S)){
				JFileChooser chooser = new JFileChooser();
				File f = new File("res/");
				chooser.setCurrentDirectory(f);
			    int returnVal = chooser.showSaveDialog(new JFrame());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	Globals.activeLevel.setLevelLocation(chooser.getSelectedFile().getAbsolutePath());
			    	Globals.activeLevel.saveLevel();
			    }
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_O)){
				JFileChooser chooser = new JFileChooser();
				File f = new File("res/");
				chooser.setCurrentDirectory(f);
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".map", ".map");
				chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(new JFrame());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	Globals.activeLevel = Level.loadLevel(chooser.getSelectedFile().getAbsolutePath());
			    }
			}
			
			if(snap){
				speedModifier = 1f;
				x = (int)x;
				y = (int)y;
				z = (int)z;
			}
				
			if(Keyboard.isKeyDown(Keyboard.KEY_W))
				x -= speedModifier;
			if(Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
				x += speedModifier;
			if(Keyboard.isKeyDown(Keyboard.KEY_A))
				y -= speedModifier;
			if(Keyboard.isKeyDown(Keyboard.KEY_D))
				y += speedModifier;
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
				z += speedModifier;
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				z -= speedModifier;
			
			if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
				for(Asset a : this.getAssets())
					a.setzRot(a.getzRot()+5);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_E)){
				for(Asset a : this.getAssets()){
					float zRot = a.getzRot()-5;
					a.setzRot(zRot);
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(snap){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			setxPos(x);
			setyPos(y);
			setzPos(z);
			return true;
		}
	};
	private File prevSelection = null;
	public LevelCreator(){
		AssetFunctions.initFunctionMap();
		am = new AssetManager();
		am.setVisible(true);
		Globals.entities.add(cursor);
		g3d = new Graphics3D();
		g3d.debug = true;
		Globals.activeLevel = Level.loadLevel("res/maps/levelCreator.map");
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
				Asset a = cursor.getAssets().get(0);
				a.setPos(cursor.getxPos(), cursor.getyPos(), cursor.getzPos());
				Globals.activeLevel.addAsset(a);
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				File f = am.getSelectedAsset();
				ArrayList<Asset> cursorAssets = new ArrayList<>();
				cursorAssets.add(Asset.loadAsset(f.getAbsolutePath()));
				cursor.setAssets(cursorAssets);
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
