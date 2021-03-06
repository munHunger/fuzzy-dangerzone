package levelUtils;

import gameLogic.Entity;

import java.util.HashMap;
import java.util.Set;

import utilities.Invoke;

public class AssetFunctions {
	private static HashMap<String, Invoke<Boolean, Entity>> functionMap = new HashMap<>();
	
	public static Boolean callFunction(String key, Entity e){
		if(!functionMap.containsKey(key))
			return false;
		Invoke<Boolean, Entity> fun = functionMap.get(key);
		return fun.call(e);
	}
	
	public static String[] getKeyList(){
		Set<String> keySet = functionMap.keySet();
		String toReturn[] = new String[keySet.size()+1];
		toReturn[0] = "NULL";
		int i = 1;
		for(String s : keySet){
			toReturn[i] = s;
			i++;
		}
		return toReturn;
	}
	
	public static void initFunctionMap(){
		functionMap.put("test", new Invoke<Boolean, Entity>(){
			public Boolean call(Entity e){
				System.out.println(e.getxPos());
				return true;
			}
		});
	}
}
