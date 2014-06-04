package utilities;

import java.util.ArrayList;

import GameLogic.Entity;
import LevelUtils.Level;
/**
 * Globals
 * @author Marcus
 * @version 1.0
 */
public class Globals {
	public static int screenWidth = 1680;
	public static int screenHeight = 1050;
	
	public static Level activeLevel = null;
	public static ArrayList<Entity> entities = new ArrayList<>();
}
