package GameEngine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import GameEngine.EntityEngine.SDistantHumanControl;
import GameEngine.EntityEngine.SEntity;
import GameEngine.SyncEngine.SFPS;
import WebEngine.SMessage;

public class SGameInstance {
	private List<SEntity> Entities = new ArrayList<SEntity>();
	private LinkedList<SMessage> ServerMessages = new LinkedList<SMessage>();
	private LinkedList<SMessage> ClientMessages = new LinkedList<SMessage>();
	private SFPS FPS;
	
	
	public SGameInstance(){
		FPS = new SFPS();
	}
	
	public List<SEntity> getEntities(){
		return Entities;
	}
	
	public SFPS getFPS(){
		return FPS;
	}
	
	public void addEntity(SEntity entity){
		Entities.add(entity);
	}
	
	protected SEntity getEntityById(UUID Id){
		for(SEntity entity : Entities){
			if (entity.getId() == Id)
				return entity;
		}
		System.out.println("Entity not found, with Id: "+Id);
		return null;
	}
	
	public void UpdateEntities(){
		for(SEntity entity : Entities){
			entity.update();
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
			SMessage message = ClientMessages.poll();
			//TODO add command check as not only entity can get a refresh!
			SEntity entity = getEntityById(message.getId());
			
			String content = message.getContent();
			while(content.contains(";")){
				String sub = content.substring(0, content.indexOf(";"));
				if (content.indexOf(";")<content.length()-1)
					content = content.substring(content.indexOf(";")+1);
				else content = "";
				// Checking sub
				if("PR".contains(sub.substring(0, 0)) &&
						"WASD".contains(sub.substring(1, 1))){
					// P-Pressed or R-Released
					boolean state;
					SDistantHumanControl control = (SDistantHumanControl)entity.getController();
					if(sub.substring(0, 0) == "P"){
						state = true;
					}else{
						state = false;
					}
					control.setKeyTo(sub.substring(1, 1), state);
					System.out.println("yap got it");
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
