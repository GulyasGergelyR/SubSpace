package WebEngine.ComEngine;
import java.net.InetAddress;

import GameEngine.SId;
import GameEngine.SIdentifiable;
import GameEngine.SPlayer;
import GameEngine.SPlayer.PlayerState;

public class SNode extends SIdentifiable{
	//////////Communication
	private InetAddress IPAddress;
	//////////Interface
	private ConnectionState state = ConnectionState.NotConnected;
	private float ping = 1.0f;
	//////////Game
	private SPlayer player;
	
	public SNode(InetAddress IPAddress, int port, SId Id, String name){
		this.IPAddress = IPAddress;
		//this.port = port;
		this.Id = Id;
		this.player = new SPlayer(this, name, PlayerState.local);
	}
	
	@Deprecated
	public SNode(InetAddress IPAddress, int port){
		super();
		this.IPAddress = IPAddress;
		this.player = new SPlayer(this, "noname", PlayerState.local);
	}

	public InetAddress getIPAddress() {
		return IPAddress;
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