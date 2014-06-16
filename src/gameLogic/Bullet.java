package gameLogic;

import java.util.ArrayList;

public class Bullet extends Entity{

	private static final long serialVersionUID = -6167506782209650747L;

	private float xDir, yDir, zDir;
	public Bullet(String entityName, ArrayList<String> assetNames) {
		super(entityName, assetNames);
	}
	
	public Bullet(float x, float y, float z, float xDir, float yDir, float zDir){
		super("", new ArrayList<String>(){/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{add("res/assets/Effects/Explosion/Explosion.asset");}});

		super.setxPos(x);
		super.setyPos(y);
		super.setzPos(z);
		this.xDir = xDir;
		this.yDir = yDir;
		this.zDir = zDir;
	}

	public void step(){
		super.setxPos(super.getxPos() + xDir);
		super.setyPos(super.getyPos() + yDir);
		super.setzPos(super.getzPos() + zDir);
	}
}