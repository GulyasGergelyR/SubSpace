package GameEngine.PlayerEngine;

import GameEngine.SId;
import GameEngine.BaseEngine.SUpdatable;
import GameEngine.EntityEngine.SEntity;
import WebEngine.ComEngine.SNode;

public class SPlayer extends SUpdatable{
	private SNode clientNode;
	private SEntity entity = null;
	private String name;
	private PlayerType playerType;
	
	public enum PlayerType{
		local, lan
	}
	
	//TODO implement scores
	private int kills;
	private int deaths;
	
	// clientNode is just for the local for everybody at server side
	public SPlayer(SNode clientNode, String name, PlayerType playerState){
		this.setClientNode(clientNode);
		this.playerType = playerState;
		this.name = name;
	}
	public SPlayer(int id, String name, PlayerType playerState){
		this.Id = new SId(id);
		this.playerType = playerState;
		this.name = name;
	}
	public SNode getClientNode() {
		return clientNode;
	}
	public void setClientNode(SNode clientNode) {
		this.clientNode = clientNode;
		this.inheritIdFrom(clientNode);
		clientNode.setPlayer(this);
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public SEntity getEntity(){
		return entity;
	}
	public void setEntity(SEntity entity){
		this.entity = entity;
		this.entity.inheritIdFrom(this);  // player inherits Id from node, now entity inherits id from player
	}
	public PlayerType getPlayerType() {
		return playerType;
	}
	public void setPlayerType(PlayerType playerState) {
		this.playerType = playerState;
	}
	public int getKills() {
		return kills;
	}
	public void addKill(int kills) {
		this.kills += kills;
	}
	public int getDeaths() {
		return deaths;
	}
	public void addDeath(int deaths) {
		this.deaths += deaths;
	}
	public void setKills(int kills) {
		this.kills = kills;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	
}
