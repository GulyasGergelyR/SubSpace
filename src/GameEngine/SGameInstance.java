package GameEngine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import GameEngine.BaseEngine.SObject;
import GameEngine.EntityEngine.SEntity;
import GameEngine.ObjectEngine.SBackGround;
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
	//private LinkedList<SMessage> ServerMessages = new LinkedList<SMessage>();
	//private LinkedList<SMessage> ClientMessages = new LinkedList<SMessage>();
	private SBackGround backGround = new SBackGround();
	
	private SFPS FPS;
	private static int delta;
	
	public SGameInstance(){
		FPS = new SFPS();
		backGround.setTexture("res/object/background/bg1.png");
		players = new ArrayList<SPlayer>();
		entities = new ArrayList<SEntity>();
		objects = new LinkedList<SObject>();
	}
	
	public List<SEntity> getEntities(){
		return entities;
	}
	public LinkedList<SObject> getObjects(){
		return objects;
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
	public void removeEntity(int Id){
		synchronized (entities) {
			SEntity entity = getEntityById(Id);
			if (entity != null){
				entities.remove(entity);
			}
		}
		synchronized (players) {
			SPlayer player = getPlayerById(Id);
			if (player != null){
				players.remove(player);
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
	}
	
	protected void UpdateEntities(){
		if(!entities.isEmpty()){
			for(SEntity entity : entities){
				entity.update();
			}
		}
	}
	protected void UpdateObjects(){
		if(!objects.isEmpty()){
			for(SObject object : objects){
				object.update();
			}
		}
	}
	
	public void SendGameDataToClients(){
		SendEntityData();
		SendObjectData();
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
					if (entity != null)
						SMParser.parseEntityUpdateMessage(message, entity);
				}
				else if (command == SMPatterns.CEntityCreate){ 	//Server creates Entity
					SMParser.parseEntityCreateMessage(message);
				}
				else if (command == SMPatterns.CEntityDelete){ 	//Server deletes an Entity
					int id = SMParser.parseId(message.getBuffer());
					SEntity entity = getEntityById(id);
					if (entity != null)
						entities.remove(entity);
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
					SEntity object = getEntityById(id);
					if (object != null)
						objects.remove(object);
				}
			}
			i++;
		}
	}
}
