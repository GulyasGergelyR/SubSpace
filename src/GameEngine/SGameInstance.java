package GameEngine;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.ObjectEngine.SBackGround;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.DebrisEngine.SDebrisFactory;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUpFactory;
import GameEngine.PlayerEngine.SPlayer.PlayerType;
import GameEngine.SyncEngine.SFPS;
import Main.SMain;
import WebEngine.ComEngine.SCommunicationHandler;
import WebEngine.ComEngine.SNode;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMParser;
import WebEngine.MessageEngine.SMPatterns;

public class SGameInstance {
	private LinkedList<SObject> animationObjects;
	private SBackGround backGround;
	
	
	private SFPS FPS;
	private static int delta;
	
	public boolean firstTime = true;
	
	public SGameInstance(){
		FPS = new SFPS();
		backGround = new SBackGround();
		backGround.getBody().setTexture("res/object/background/bg1.png");
		animationObjects = new LinkedList<SObject>();
		
		// Factories
		SFH.initFactories();
	}
	
	public LinkedList<SObject> getAnimationObjects(){
		return animationObjects;
	}
	
	public SFPS getFPS(){
		return FPS;
	}
	public void updateDelta(){
		delta = FPS.getDelta();
	}
	public int getDelta(){
		return delta;
	}
	public float getDeltaRatio(){
		//TODO check this fps
		return ((float)delta)/FPS.getFPS_M();
	}

	public SBackGround getBackGround(){
		return backGround;
	}
	public void addAnimationObject(SObject object){
		animationObjects.add(object);
	}
	
	public void UpdateGame(){
//		if (firstTime && SMain.IsServer()){
//			try {
//				byte[] address = new byte[]{(byte)192,(byte)168,1,2};
//				SNode client = new SNode(InetAddress.getByAddress(address), 5000, "Dummy", PlayerType.lan);
//				synchronized (SMain.getCommunicationHandler().getNodes()) {
//					SMain.getCommunicationHandler().getNodes().add(client);
//				SFH.Entities.createEntityAtServer(client.getId().get());
//				}
//			} catch (UnknownHostException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			firstTime = false;
//		}
		
		if (SMain.IsServer()){
			SFH.Entities.collisionCheckInFactory();
		}
		SFH.Entities.UpdateObjects();
		UpdateAnimationObjects();
		UpdateFactories();
	}
	
	protected void UpdateFactories(){
		if (SMain.IsServer()){
			Random random = new Random();
			if (Specifications.allowPowerUps){
				if (random.nextFloat()>Specifications.chanceForHeal){
					SFH.PowerUps.tryToCreateNewPowerUpAtServer(SPowerUpFactory.PowerUpHeal);
				}
				if (random.nextFloat()>Specifications.chanceForBurst){
					SFH.PowerUps.tryToCreateNewPowerUpAtServer(SPowerUpFactory.PowerUpBurst);
				}
				if (random.nextFloat()>Specifications.chanceForForceBoost){
					SFH.PowerUps.tryToCreateNewPowerUpAtServer(SPowerUpFactory.PowerUpForceBoost);
				}
				if (random.nextFloat()>Specifications.chanceForBull){
					SFH.PowerUps.tryToCreateNewPowerUpAtServer(SPowerUpFactory.PowerUpBull);
				}
			}
			if (random.nextFloat()>Specifications.chanceForAsteroid){
				SFH.Debris.tryToCreateNewDebrisAtServer(SDebrisFactory.Asteroid);
			}
			SFH.Debris.collisionCheckInFactory();
		}
		SFH.Bullets.UpdateObjects();
		SFH.Effects.UpdateObjects();
		SFH.PowerUps.UpdateObjects();
		SFH.Debris.UpdateObjects();
	}
	protected void UpdateAnimationObjects(){
		if(!animationObjects.isEmpty()){
			ListIterator<SObject> iter = animationObjects.listIterator();
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
	
	public void CheckMessages(){
		CheckEntityMessages();
		CheckObjectMessages();
	}
	
	
	protected void CheckEntityMessages(){
		SCommunicationHandler communicationHandler = SMain.getCommunicationHandler();
		// Check Entity messages
		int current_length = communicationHandler.getEntityMessageLength();
		int i = 0;
		while(i < current_length){
			SM message = communicationHandler.popEntityMessage();
			byte command = message.getCommandId();
			if (SMain.IsServer()){  // Client input
				if (command == SMPatterns.CClientInput){ 	//Client input (pressed key, mouse moved, mouse click)
					int id = SMParser.parseId(message.getBuffer());
					SEntity entity = SFH.Entities.getObjectById(id);
					if (entity != null){
						SMParser.parseClientInputMessage(message, entity);
					}
				}
				else if (command == SMPatterns.CEntityCreateAtServer){ 	//Server side communication handler sent this, NOT A CLIENT!
					SMParser.parseEntityCreateMessageAtServer(message);
				}
			}else{
				if (command == SMPatterns.CEntityUpdate){ 	//Server updates Entity information
					int id = SMParser.parseId(message.getBuffer());
					SEntity entity = SFH.Entities.getObjectById(id);
					if (entity != null){
						SMParser.parseEntityUpdateMessage(message, entity);
					}
				}
				else if (command == SMPatterns.CEntityUpdateState){ 	//Server updates Entity information
					int id = SMParser.parseId(message.getBuffer());
					SEntity entity = SFH.Entities.getObjectById(id);
					if (entity != null){
						SMParser.parseEntityUpdateStateMessage(message, entity);
					}
				}
				else if (command == SMPatterns.CEntityCreate){ 	//Server creates Entity
					SMParser.parseEntityCreateMessageAtClient(message);
				}
				else if (command == SMPatterns.CEntityDelete){ 	//Server deletes an Entity
					int id = SMParser.parseId(message.getBuffer());
					SFH.Entities.removeEntity(id);
				}
			}
			i++;
		}
	}
	
	protected void CheckObjectMessages(){
		SCommunicationHandler communicationHandler = SMain.getCommunicationHandler();
		// Check Object messages
		int current_length = communicationHandler.getObjectMessageLength();
		int i = 0;
		while(i < current_length){
			SM message = communicationHandler.popObjectMessage();
			byte command = message.getCommandId();
			if (SMain.IsServer()){  // Client input
				if (command == SMPatterns.CObjectRequestCreate){ 	//Client input (pressed key, mouse moved, mouse click)
					SMParser.parseObjectRequestCreateMessage(message);
				}
			}else{
				if (command == SMPatterns.CObjectCreate){ 	//Server created Object
					SMParser.parseObjectCreateMessage(message);
				}
				else if (command == SMPatterns.CObjectUpdate){ 	//Server updates Object information
						SMParser.parseObjectUpdateMessage(message);
				}
				else if (command == SMPatterns.CObjectDelete){ 	//Server deleted Object
					SMParser.parseObjectDeleteMessage(message, this);
				}
				else if (command == SMPatterns.CAnimationObjectCreate){ 	//Server created Animation Object
					//TODO add explosion generation only at client side
					SMParser.parseAnimationObjectCreateMessage(message);
				}
			}
			i++;
		}
	}
}
