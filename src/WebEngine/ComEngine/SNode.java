package WebEngine.ComEngine;
import java.net.InetAddress;
import java.util.UUID;

import GameEngine.SPlayer;

public class SNode {
	//////////Communication
	private InetAddress IPAddress;
	//private int port;
	private UUID Id;
	//////////Interface
	private NodeState state = NodeState.NotConnected;
	private float ping = 1.0f;
	//////////Game
	private SPlayer player;
	
	public SNode(InetAddress IPAddress, int port, UUID Id, String name){
		this.IPAddress = IPAddress;
		//this.port = port;
		this.Id = Id;
		this.player = new SPlayer(this, name);
	}
	public SNode(InetAddress IPAddress, int port){
		this.IPAddress = IPAddress;
		//this.port = port;
		this.Id = UUID.randomUUID();
		this.player = new SPlayer(this, "noname");
	}

	public InetAddress getIPAddress() {
		return IPAddress;
	}

	//public int getPort() {
	//	return port;
	//}

	public UUID getId() {
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

	public NodeState getState() {
		return state;
	}

	public void setState(NodeState state) {
		this.state = state;
	}

	public enum NodeState{
		Connected, NotConnected
	}

	public void setPlayer(SPlayer player) {
		this.player = player;
		
	}
	public SPlayer getPlayer(){
		return player;
	}
}