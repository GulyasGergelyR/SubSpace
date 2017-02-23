package GameEngine.ObjectEngine.DebrisEngine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import GameEngine.SId;
import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SObject.ObjectState;
import GameEngine.GeomEngine.SGeomFunctions;
import GameEngine.GeomEngine.SVector;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SDebrisFactory {
	public static final byte Asteroid = 1;
	protected static int currentNumberOfAsteroids = 0;
	protected static final int NumberOfAsteroid = 80;
	
	protected static String FactoryName = "DebrisFactory";
	
	
	public static void createNewDebrisAtClient(SVector pos, SVector moveDir, float scale, int id, byte debrisType){
		if (debrisType == Asteroid){
			SAsteroid asteroid = new SAsteroid(pos, moveDir, scale);
			asteroid.setId(new SId(id));
			addObject(asteroid);
		}
	}
	
	public static void tryToCreateNewDebrisAtServer(byte debrisType){
		if (debrisType == Asteroid){
			if (currentNumberOfAsteroids >= NumberOfAsteroid)
				return;
			Random random = new Random();
			int section = random.nextInt(4);
			float rate = random.nextFloat()*100.0f;
			float speed = 30 * rate*rate / 10000.0f + 5;
			float scale = 4 * (1 - rate*rate / 10000.0f) + 0.8f;
			
			SVector pos = new SVector();
			SVector moveDir = new SVector();
			if (section==0){
				pos = new SVector(random.nextFloat()*200-100-5000, 
						  random.nextFloat()*10200-5100);
				moveDir = new SVector(random.nextFloat() , (random.nextFloat()*2 - 1)).setLength(speed);
			}else if (section == 1){
				pos = new SVector(random.nextFloat()*10200-5100, 
						  random.nextFloat()*200-100+5000);
				moveDir = new SVector((random.nextFloat()*2 - 1), -random.nextFloat()).setLength(speed);
			}else if (section == 2){
				pos = new SVector(random.nextFloat()*200-100+5000, 
						  random.nextFloat()*10200-5100);
				moveDir = new SVector(-random.nextFloat(), (random.nextFloat()*2 - 1)).setLength(speed);
			}else {
				pos = new SVector(random.nextFloat()*10200-5100, 
						  random.nextFloat()*200-100-5000);
				moveDir = new SVector((random.nextFloat()*2 - 1), random.nextFloat()).setLength(speed);
			}
			
			SAsteroid asteroid = new SAsteroid(pos, moveDir, scale);
			addObject(asteroid);
			SM message = SMPatterns.getObjectCreateMessage(asteroid);
			SMain.getCommunicationHandler().SendMessage(message);
			currentNumberOfAsteroids++;
		}
	}
	public static void deletedDebris(byte debrisType){
		if (debrisType == Asteroid){
			currentNumberOfAsteroids--;
		}
	}
	
	public static void collisionCheckInFactory(){
		// Asteroid checks
		// Hack around types:
		// we build an array list which has a better performance for this
		boolean[] update = new boolean[objects.size()];
		
		ArrayList<SObject> temps = new ArrayList<SObject>(objects);
		for (int i=0; i<temps.size()-1; i++){
			SObject currentObject = temps.get(i);
			if (currentObject instanceof SAsteroid && 
					currentObject.getObjectState().equals(ObjectState.Active)){
				for (int j=i+1;j<temps.size(); j++){
					SObject contra = temps.get(j);
					if (contra instanceof SAsteroid &&
						contra.getObjectState().equals(ObjectState.Active) &&
						!contra.equals(currentObject)){
						if (SGeomFunctions.intersects(contra, currentObject)){
							if (SGeomFunctions.collide((SAsteroid)currentObject, (SAsteroid)contra)){
								update[i] = true;
								update[j] = true;
							}
						}
					}
				}
			}
		}
		for (int i=0; i< update.length; i++){
			if (update[i]){
				SAsteroid asteroid = (SAsteroid)temps.get(i);
				asteroid.getController().setSendCounter(0);
				SM message = SMPatterns.getObjectUpdateMessage(asteroid);
				SMain.getCommunicationHandler().SendMessage(message);
			}
		}
	}
	
	protected static LinkedList<SObject> objects;
	
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
		System.out.printf("Object was not found in '%s', with Id: "+Id+"\n", FactoryName);
		return null;
	}
}
