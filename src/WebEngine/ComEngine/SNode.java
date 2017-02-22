package WebEngine.ComEngine;
import java.net.InetAddress;

import GameEngine.SId;
import GameEngine.SIdentifiable;
import GameEngine.SPlayer;
import GameEngine.SPlayer.PlayerType;

public class SNode extends SIdentifiable{
	private InetAddress address;
	public enum ConnectionState{
		Connected, NotConnected
	}
	private ConnectionState state = ConnectionState.NotConnected;
	private float ping = 1.0f;
	private SPlayer player;
	
	public SNode(InetAddress address, int port, String name, PlayerType playerState){
		super();
		this.address = address;
		this.player = new SPlayer(this, name, playerState);
	}
	
	public SNode(InetAddress address, int port, int id){
		this.Id = new SId(id);
		this.address = address;
	}

	public InetAddress getAddress() {
		return address;
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

	public void setPlayer(SPlayer player) {
		this.player = player;
		player.inheritIdFrom(this);
	}
	public SPlayer getPlayer(){
		return player;
	}
}