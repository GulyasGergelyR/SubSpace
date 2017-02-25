package GameEngine.ObjectEngine;

import java.util.LinkedList;
import java.util.ListIterator;

import GameEngine.BaseEngine.SUpdatable;

public class SFactory<Type> {
	protected LinkedList<Type> objects;
	protected String FactoryName = "None";
	
	public SFactory(){
		objects = new LinkedList<Type>();
	}
	
	public void UpdateObjects(){
		if(!objects.isEmpty()){
			ListIterator<Type> iter = objects.listIterator();
			while(iter.hasNext()){
				SUpdatable object = (SUpdatable)iter.next();
			    if(object.shouldBeDeleted()){
			    	object.kill();
			        iter.remove();
			    }else {
			    	object.update();
			    	if(object.shouldBeDeleted()){
			    		object.kill();
				        iter.remove();
				    }
			    }
			}
		}
	}
	
	public void addObject(Type object){
		objects.add(object);
	}
	
	public LinkedList<Type> getObjects(){
		return objects;
	}
	
	public void removeObjectFromList(int Id){
		ListIterator<Type> iter = objects.listIterator();
		while(iter.hasNext()){
			SUpdatable object = (SUpdatable)iter.next();
		    if(object.equals(Id)){
		    	object.kill();
		        iter.remove();
		        break;
		    }
		}
	}
	
	public Type getObjectById(int Id){
		for(Type object : objects){
			if (object.equals(Id))
				return (Type)object;
		}
		System.out.printf("Object was not found in '%s' factory, with Id: "+Id+"\n", FactoryName);
		return null;
	}
}
