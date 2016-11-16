package GameEngine;

import GameEngine.EntityEngine.SEntity;

public class SId {
	static int currentMaxDefaultObjectId = 1;
	static int currentMaxEntityId = 1;
			
	public static int getNewId(Object o){
		if (o instanceof SEntity){
			return currentMaxEntityId++;
		}
		else{
			return currentMaxDefaultObjectId++;
		}
	}
}
