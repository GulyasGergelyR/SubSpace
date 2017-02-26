package GameEngine.ObjectEngine;

import java.util.LinkedList;
import java.util.ListIterator;

import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;
import GameEngine.SIdentifiable;
import GameEngine.BaseEngine.SUpdatable;
import Main.SMain;

public class SFactory<Type> {
	protected LinkedList<Type> objects;
	protected String FactoryName = "None";
	protected byte factoryType;
	
	public SFactory(String name, byte id){
		objects = new LinkedList<Type>();
		FactoryName = name;
		factoryType = id;
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
		if (getObjectById(((SIdentifiable)object).getId().get()) == null){
			objects.add(object);
		}
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
	
	public byte getFactoryType(){
		return factoryType;
	}
	
	public Type getObjectById(int Id){
		for(Type object : objects){
			if (object.equals(Id))
				return (Type)object;
		}
		return null;
	}
	
	public Type getObjectByIdWithCheck(int Id){
		for(Type object : objects){
			if (object.equals(Id))
				return (Type)object;
		}
		System.out.printf("Object was not found in '%s' factory, with Id: "+Id+"\n", FactoryName);
		SM message = SMPatterns.getObjectRequestCreateMessage(Id, factoryType);
	    SMain.getCommunicationHandler().SendMessage(message);
		return null;
	}
}
