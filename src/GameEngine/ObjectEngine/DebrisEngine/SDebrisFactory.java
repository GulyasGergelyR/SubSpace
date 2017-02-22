package GameEngine.ObjectEngine.DebrisEngine;

import java.util.Random;
import java.util.Vector;

import GameEngine.SId;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFactory;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SDebrisFactory extends SFactory {
	public static final byte Asteroid = 1;
	protected static int currentNumberOfAsteroids = 0;
	protected static final int NumberOfAsteroid = 50;
	
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
			float rate = random.nextFloat()*90.0f + 10.0f;
			float speed = 25 * rate*rate / 10000.0f + 5;
			float scale = 4 * (1 - rate*rate / 10000.0f) + 1.0f;
			
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
				
			
			//SVector moveDir = new SVector((random.nextFloat()*2 - 1) * speed, (random.nextFloat()*2 - 1) * speed);
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
}
