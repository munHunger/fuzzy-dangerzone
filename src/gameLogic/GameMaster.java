package gameLogic;

import java.util.ArrayList;

public class GameMaster implements Runnable{
	public static ArrayList<Entity> entities = new ArrayList<>();

	private final static long stepLength = 100;
	
	public void run() {
		while(true){
			long time = System.currentTimeMillis();
			for(Entity e : entities)
				e.step();
			if(System.currentTimeMillis()-time < stepLength){
				try {
					Thread.sleep(stepLength-(System.currentTimeMillis()-time));
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
