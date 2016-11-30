package GameEngine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.swing.text.html.parser.Entity;

import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SObject.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SBackGround;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUpFactory;
import GameEngine.SyncEngine.SFPS;
import Main.SMain;
import WebEngine.ComEngine.SCommunicationHandler;
import WebEngine.ComEngine.SCommunicationHandler.UDPRole;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMParser;
import WebEngine.MessageEngine.SMPatterns;

public class SGameInstance {
	@Deprecated
	private SPlayer localPlayer;
	
	//TODO decide whether to use player or entity array!
	
	private List<SPlayer> players;
	private List<SEntity> entities;
	private LinkedList<SObject> objects;
	private LinkedList<SObject> animationObjects;
	private SBackGround backGround = new SBackGround();
	
	
	private SFPS FPS;
	private static int delta;
	
	public SGameInstance(){
		FPS = new SFPS();
		backGround.getBody().setTexture("res/object/background/bg1.png");
		players = new ArrayList<SPlayer>();
		entities = new ArrayList<SEntity>();
		objects = new LinkedList<SObject>();
		animationObjects = new LinkedList<SObject>();
	}
	public List<SPlayer> getPlayers(){
		return players;
	}
	public List<SEntity> getEntities(){
		return entities;
	}
	public LinkedList<SObject> getObjects(){
		return objects;
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
	public void addPlayer(SPlayer player){
		players.add(player);
	}
	public void removePlayer(SPlayer player){
		players.remove(player);
	}
	public SPlayer getLocalPlayer() {
		return localPlayer;
	}

	public void setLocalPlayer(SPlayer localPlayer) {
		this.localPlayer = localPlayer;
	}

	public SBackGround getBackGround(){
		return backGround;
	}
	
	public void addEntity(SEntity entity){
		synchronized (entities) {
			entities.add(entity);
		}
	}
	public void addObject(SObject object){
		objects.add(object);
	}
	public void addAnimationObject(SObject object){
		animationObjects.add(object);
	}
	public void removeEntity(int Id){
		synchronized (entities) {
			synchronized (players) {
				removeEntityFromList(Id);
				removePlayerFromList(Id);
			}
		}
	}
	private void removePlayerFromList(int Id){
		ListIterator<SPlayer> iter = players.listIterator();
		while(iter.hasNext()){
			SPlayer player = iter.next();
		    if(player.equals(Id)){
		        iter.remove();
		        break;
		    }
		}
	}
	private void removeEntityFromList(int Id){
		ListIterator<SEntity> iter = entities.listIterator();
		while(iter.hasNext()){
			SEntity entity = iter.next();
		    if(entity.equals(Id)){
		        iter.remove();
		        break;
		    }
		}
	}
	private void removeObjectFromList(int Id){
		ListIterator<SObject> iter = objects.listIterator();
		while(iter.hasNext()){
			SObject object = iter.next();
		    if(object.equals(Id)){
		        iter.remove();
		        break;
		    }
		}
	}
	
	public SEntity getEntityById(int Id){
		for(SEntity entity : entities){
			if (entity.equals(Id))
				return entity;
		}
		System.out.println("Entity was not found, with Id: "+Id);
		return null;
	}
	
	public SPlayer getPlayerById(int Id){
		for(SPlayer player : players){
			if (player.equals(Id))
				return player;
		}
		System.out.println("Player was not found, with Id: "+Id);
		return null;
	}
	
	public SObject getObjectById(int Id){
		for(SObject object : objects){
			if (object.equals(Id))
				return object;
		}
		System.out.println("Object was not found, with Id: "+Id);
		return null;
	}
	
	public void UpdateGame(){
		UpdateEntities();
		UpdateObjects();
		UpdateAnimationObjects();
		UpdateFactories();
	}
	
	protected void UpdateFactories(){
		if (SMain.IsServer()){
			Random random = new Random();
			if (random.nextFloat()>0.9f){
				SPowerUpFactory.tryToCreateNewPowerUpAtServer(
						new SVector(random.nextFloat()*8000 -4000, random.nextFloat()*8000 -4000),
						SPowerUpFactory.PowerUpHeal);
			}
		}
	}
	
	protected void UpdateEntities(){
		if(!entities.isEmpty()){
			ListIterator<SEntity> iter = entities.listIterator();
			while(iter.hasNext()){
				SEntity entity = iter.next();
				if(entity.getObjectState().equals(ObjectState.WaitingDelete)){
			        iter.remove();
			        removePlayerFromList(entity.getId().get());
				}else if(entity.getObjectState().equals(ObjectState.Initialization)){
				        for(SPlayer player : players){
				        	if (!player.equals(entity)){
				        		SM message = SMPatterns.getEntityCreateMessage(player);
				        		SMain.getCommunicationHandler().SendMessageToNode(message, entity.getId().get());
						        SM messageEntityState = SMPatterns.getEntityUpdateStateMessage(player.getEntity());
					        	SMain.getCommunicationHandler().SendMessageToNode(messageEntityState, entity.getId().get());
				        	}
				        }
				        for(SObject object : objects){
				        	SM message = SMPatterns.getObjectCreateMessage(object);
				        	SMain.getCommunicationHandler().SendMessageToNode(message, entity.getId().get());
				        }
				        entity.setObjectState(ObjectState.Active);
				        SM message = SMPatterns.getEntityUpdateStateMessage(entity);
			        	SMain.getCommunicationHandler().SendMessage(message);
			    }else {
			    	entity.update();
			    	if(entity.getObjectState().equals(ObjectState.WaitingDelete)){
				        iter.remove();
				        removePlayerFromList(entity.getId().get());
				    }
			    }
			}
		}
	}
	protected void UpdateObjects(){
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
	
	public void SendGameDataToClients(){
		SendEntityData();
		//SendObjectData();
	}
	private void SendEntityData(){
		synchronized (entities) {
			for(SEntity entity: entities){
				SM message = SMPatterns.getEntityUpdateMessage(entity);
				SMain.getCommunicationHandler().SendMessage(message);
			}
		}
	}
	private void SendObjectData(){
		synchronized (objects) {
			for(SObject object: objects){
				SM message = SMPatterns.getObjectUpdateMessage(object);
				SMain.getCommunicationHandler().SendMessage(message);
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
			if (SMain.getCommunicationHandler().getUDPRole().equals(UDPRole.Server)){  // Client input
				if (command == SMPatterns.CClientInput){ 	//Client input (pressed key, mouse moved, mouse click)
					int id = SMParser.parseId(message.getBuffer());
					SEntity entity = getEntityById(id);
					if (entity != null){
						SMParser.parseClientInputMessage(message, entity);
					}
				}
			}else{
				if (command == SMPatterns.CEntityUpdate){ 	//Server updates Entity information
					int id = SMParser.parseId(message.getBuffer());
					SEntity entity = getEntityById(id);
					if (entity != null){
						SMParser.parseEntityUpdateMessage(message, entity);
					}
				}
				if (command == SMPatterns.CEntityUpdateState){ 	//Server updates Entity information
					int id = SMParser.parseId(message.getBuffer());
					SEntity entity = getEntityById(id);
					if (entity != null){
						SMParser.parseEntityUpdateStateMessage(message, entity);
					}
				}
				else if (command == SMPatterns.CEntityCreate){ 	//Server creates Entity
					SMParser.parseEntityCreateMessage(message);
				}
				else if (command == SMPatterns.CEntityDelete){ 	//Server deletes an Entity
					int id = SMParser.parseId(message.getBuffer());
					removeEntity(id);
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
			if (SMain.getCommunicationHandler().getUDPRole().equals(UDPRole.Server)){  // Client input
				
			}else{
				if (command == SMPatterns.CObjectCreate){ 	//Server created Object
					SMParser.parseObjectCreateMessage(message);
				}
				else if (command == SMPatterns.CObjectUpdate){ 	//Server updates Object information
					int id = SMParser.parseId(message.getBuffer());
					SObject object = getObjectById(id);
					if (object != null)
						SMParser.parseObjectUpdateMessage(message, object);
				}
				else if (command == SMPatterns.CObjectDelete){ 	//Server deleted Object
					int id = SMParser.parseId(message.getBuffer());
					removeObjectFromList(id);
				}
				else if (command == SMPatterns.CAnimationObjectCreate){ 	//Server deleted Object
					SMParser.parseAnimationObjectCreateMessage(message);
				}
			}
			i++;
		}
	}
}
