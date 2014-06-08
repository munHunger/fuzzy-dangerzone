package levelUtils;

public class LevelCreator {

	public LevelCreator(){
		AssetFunctions.initFunctionMap();
		AssetManager am = new AssetManager();
		am.setVisible(true);
	}
	
	public static void main(String args[]){
		new LevelCreator();
	}
}
