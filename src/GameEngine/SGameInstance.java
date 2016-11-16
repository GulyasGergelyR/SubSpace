package GameEngine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SHumanControl;
import GameEngine.ObjectEngine.SBackGround;
import GameEngine.SyncEngine.SFPS;
import Main.SMain;
import WebEngine.ComEngine.SCommunicationHandler;
import WebEngine.ComEngine.SMessage;
import WebEngine.ComEngine.SMessageParser;

public class SGameInstance {
	private List<SEntity> Entities = new ArrayList<SEntity>();
	private LinkedList<SMessage> ServerMessages = new LinkedList<SMessage>();
	private LinkedList<SMessage> ClientMessages = new LinkedList<SMessage>();
	private SBackGround backGround = new SBackGround();
	
	private SFPS FPS;
	private static int delta;
	
	private SPlayer localPlayer;
	
	public SGameInstance(){
		FPS = new SFPS();
		backGround.setTexture("res/object/background/bg1.png");
	}
	
	public List<SEntity> getEntities(){
		return Entities;
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
	
	public SPlayer getLocalPlayer() {
		return localPlayer;
	}

	public void setLocalPlayer(SPlayer localPlayer) {
		this.localPlayer = localPlayer;
		addEntity(localPlayer.getEntity());
	}

	public SBackGround getBackGround(){
		return backGround;
	}
	
	public void addEntity(SEntity entity){
		synchronized (Entities) {
			Entities.add(entity);
		}
	}
	public boolean removeEntity(int Id){
		synchronized (Entities) {
			SEntity entity = getEntityById(Id);
			if (entity != null){
				Entities.remove(entity);
				return true;
			}
			else return false;
		}
	}
	
	protected SEntity getEntityById(int Id){
		for(SEntity entity : Entities){
			if (entity.getId() == Id)
				return entity;
		}
		System.out.println("Entity was not found, with Id: "+Id);
		return null;
	}
	
	public void UpdateEntities(){
		if(Entities.size()>0){
			int maxLength = SMain.getCommunicationHandler().getEntityMessageLength();
			
			for(SEntity entity : Entities){
				for(SMessage message : SMain.getCommunicationHandler().getEntityMessagesForEntity(entity, maxLength)){
					if(message.getCommandName().equals("ENTUP"))
						SMessageParser.ParseEntityUpdateMessage(message, entity);
					else if(message.getCommandName().equals("CLIIN"))
						SMessageParser.ParseClientInputMessage(message, entity);
				}
				
				entity.update();
			}
			//get rest of the messages
			for(SMessage message : SMain.getCommunicationHandler().getEntityMessages()){
				if(message.getCommandName().equals("ENTCR"))
					SMessageParser.ParseEntityCreateMessage(message);
			}
		}
	}
	
	public void SendGameDataToClients(){
		SendEntityData();
	}
	private void SendEntityData(){
		synchronized (Entities) {
			for(SEntity entity: Entities){
				SMessage message = new SMessage(entity.getId(), "ENTUP", "");
				message.addContent("p;"+entity.getPos().getString());
				message.addContent("md;"+entity.getMoveDir().getString());
				message.addContent("ld;"+entity.getLookDir().getString());
				message.addContent("ad;"+entity.getAcclDir().getString());
				SMain.getCommunicationHandler().SendMessage(message);
			}
			
		}
	}
	
	public void CheckEntityMessages(){
		SCommunicationHandler communicationHandler = SMain.getCommunicationHandler();
		// Check Entity messages (server do not receive Obj message, yet)
		int current_length = communicationHandler.getEntityMessageLength();
		int i = 0;
		while(i<current_length){
			SMessage message = communicationHandler.popEntityMessage();
			if(message.getCommandName().equals("CLIIN")){  // Client input
				
			}
			i++;
		}
	}
	
	
	public void CheckServerMessages(){
		int current_length = ServerMessages.size();
		int i = 0;
		while(i<current_length){
			SMessage message = ServerMessages.poll();
			
		}
	}
	
	public void CheckClientMessages(){
		int current_length = ClientMessages.size();
		int i = 0;

		while(i<current_length){
			i += 1;
			SMessage message = ClientMessages.poll();
			
			SEntity entity = getEntityById(message.getId());
			
			String content = message.getContent();
			while(content.contains(";")){
				String sub = content.substring(0, content.indexOf(";"));
				if (content.indexOf(";")<content.length()-1)
					content = content.substring(content.indexOf(";")+1);
				else content = "";
				//System.out.println(sub+" "+sub.substring(0, 1)+" "+sub.substring(1, 2)+" "+content);
				// Checking sub
				if("PR".contains(sub.substring(0, 1)) &&
						"WASD".contains(sub.substring(1, 2))){
					// P-Pressed or R-Released
					
					boolean state;
					SHumanControl control = (SHumanControl)entity.getController();
					if(sub.substring(0, 1) == "P"){
						state = true;
					}else{
						state = false;
					}
					
					control.setKeyTo(sub.substring(1, 2), state);
				}
			}
		}
	}
	
	public void AddServerMessage(SMessage message){
		ServerMessages.add(message);
	}
	public void AddClientMessage(SMessage message){
		ClientMessages.add(message);
	}
}
