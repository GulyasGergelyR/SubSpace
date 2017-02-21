package GameEngine.ObjectEngine;

import java.util.LinkedList;
import java.util.ListIterator;

import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SObject.ObjectState;

public class SFactory {
	protected static LinkedList<SObject> objects;
	protected static String FactoryName = "None";
	
	public static void init(){
		objects = new LinkedList<SObject>();
	}
	
	public static void UpdateObjects(){
		if(!objects.isEmpty()){
			ListIterator<SObject> iter = objects.listIterator();
			while(iter.hasNext()){
				SObject object = iter.next();
			    if(object.getObjectState().equals(ObjectState.WaitingDelete)){
			        iter.remove();
			    }else {
			    	object.update();
			    	if(object.getObjectState().equals(ObjectState.WaitingDelete)){
				        iter.remove();
				    }
			    }
			}
		}
	}
	
	public static void addObject(SObject object){
		objects.add(object);
	}
	
	public static LinkedList<SObject> getObjects(){
		return objects;
	}
	
	public static void removeObjectFromList(int Id){
		ListIterator<SObject> iter = objects.listIterator();
		while(iter.hasNext()){
			SObject object = iter.next();
		    if(object.equals(Id)){
		        iter.remove();
		        break;
		    }
		}
	}
	
	public static SObject getObjectById(int Id){
		for(SObject object : objects){
			if (object.equals(Id))
				return object;
		}
		System.out.printf("Object was not found in '%s', with Id: "+Id, FactoryName);
		return null;
	}
}
