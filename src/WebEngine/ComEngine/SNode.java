package WebEngine.ComEngine;
import java.net.InetAddress;
import java.util.UUID;

import GameEngine.SId;
import GameEngine.SPlayer;

public class SNode {
	//////////Communication
	private InetAddress IPAddress;
	//private int port;
	private int Id;
	//////////Interface
	private ConnectionState state = ConnectionState.NotConnected;
	private float ping = 1.0f;
	//////////Game
	private SPlayer player;
	
	public SNode(InetAddress IPAddress, int port, int Id, String name){
		this.IPAddress = IPAddress;
		//this.port = port;
		this.Id = Id;
		this.player = new SPlayer(this, name);
	}
	public SNode(InetAddress IPAddress, int port){
		this.IPAddress = IPAddress;
		//this.port = port;
		this.Id = SId.getNewId(this)
		this.player = new SPlayer(this, "noname");
	}

	public InetAddress getIPAddress() {
		return IPAddress;
	}

	public int getId() {
		return Id;
	}

	public void setId(UUID id) {
		Id = id;
	}
	
	public float getPing() {
		return ping;
	}

	public void setPing(float ping) {
		this.ping = ping;
	}
	
	public String getName() {
		return player.getName();
	}

	public void setName(String name) {
		this.player.setName(name);
	}

	public ConnectionState getState() {
		return state;
	}

	public void setState(ConnectionState state) {
		this.state = state;
	}

	public enum ConnectionState{
		Connected, NotConnected
	}

	public void setPlayer(SPlayer player) {
		this.player = player;
		
	}
	public SPlayer getPlayer(){
		return player;
	}
}