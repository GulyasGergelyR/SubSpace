package GameEngine.ObjectEngine;

import java.util.LinkedList;
import java.util.ListIterator;

import GameEngine.BaseEngine.SUpdatable;

public class SFactory<Type> {
	protected LinkedList<SUpdatable> objects;
	protected String FactoryName = "None";
	
	public SFactory(){
		objects = new LinkedList<SUpdatable>();
	}
	
	public void UpdateObjects(){
		if(!objects.isEmpty()){
			ListIterator<SUpdatable> iter = objects.listIterator();
			while(iter.hasNext()){
				SUpdatable object = iter.next();
			    if(object.shouldBeDeleted()){
			        iter.remove();
			    }else {
			    	object.update();
			    	if(object.shouldBeDeleted()){
				        iter.remove();
				    }
			    }
			}
		}
	}
	
	public void addObject(SUpdatable object){
		objects.add(object);
	}
	
	public LinkedList<SUpdatable> getObjects(){
		return objects;
	}
	
	public void removeObjectFromList(int Id){
		ListIterator<SUpdatable> iter = objects.listIterator();
		while(iter.hasNext()){
			SUpdatable object = iter.next();
		    if(object.equals(Id)){
		        iter.remove();
		        break;
		    }
		}
	}
	
	@SuppressWarnings("unchecked")
	public Type getObjectById(int Id){
		for(SUpdatable object : objects){
			if (object.equals(Id))
				return (Type)object;
		}
		System.out.printf("Object was not found in '%s' factory, with Id: "+Id+"\n", FactoryName);
		return null;
	}
}
