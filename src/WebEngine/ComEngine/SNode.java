package WebEngine.ComEngine;
import java.net.InetAddress;
import java.util.UUID;

import GameEngine.SPlayer;

public class SNode {
	private InetAddress IPAddress;
	private int port;
	private UUID Id;
	private float ping;
	private ClientState state = ClientState.Init;
	
	private SPlayer player;
	
	
	public SNode(InetAddress IPAddress, int port, UUID Id, String name){
		this.IPAddress = IPAddress;
		this.port = port;
		this.Id = Id;
		this.player = new SPlayer(this, name);
	}
	public SNode(InetAddress IPAddress, int port){
		this.IPAddress = IPAddress;
		this.port = port;
		this.Id = null;
		this.player = null;
	}

	public InetAddress getIPAddress() {
		return IPAddress;
	}

	public int getPort() {
		return port;
	}

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

	public ClientState getState() {
		return state;
	}

	public void setState(ClientState state) {
		this.state = state;
	}

	public enum ClientState{
		Init, Online, Offline 
	}

	public void addPlayer(SPlayer player) {
		this.player = player;
		
	}
}