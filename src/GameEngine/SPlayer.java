package GameEngine;

import GameEngine.EntityEngine.SEntity;
import WebEngine.ComEngine.SNode;

public class SPlayer {
	private SNode client;
	private SEntity entity;
	private String name;
	
	//TODO implement scores
	private int kills;
	private int deaths;
	
	public SPlayer(SNode client, String name){
		this.client = client;
		this.name = name;
		client.setPlayer(this);
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public SNode getClient(){
		return client;
	}
	public SEntity getEntity(){
		return entity;
	}
	public void setEntity(SEntity entity){
		this.entity = entity;
	}
}
