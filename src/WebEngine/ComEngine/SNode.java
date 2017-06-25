package WebEngine.ComEngine;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.LinkedList;

import GameEngine.SId;
import GameEngine.SIdentifiable;
import GameEngine.Specifications;
import GameEngine.PlayerEngine.SPlayer;
import GameEngine.PlayerEngine.SPlayer.PlayerType;
import WebEngine.MessageEngine.SM;

public class SNode extends SIdentifiable{
	private InetAddress address;
	public enum ConnectionState{
		Connected, NotConnected
	}
	private ConnectionState state = ConnectionState.NotConnected;
	private float ping = 1.0f;
	private SPlayer player;
	
	private LinkedList<SM> messages = new LinkedList<SM>();
	
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
	
	public void addMessage(SM message){
		synchronized (messages) {
			messages.add(message);
		}
	}
	public boolean hasMessages(){
		synchronized (messages) {
			return messages.size() > 0;
		}
	}
	public byte[] flushMessages(){
		byte[] data = new byte[Specifications.MessageLength];
		ByteBuffer buffer = ByteBuffer.wrap(data);
		synchronized (messages) {
			// System.out.println("Flushing: "+ messages.size());
			while (messages.size() > 0){
				SM message = messages.pop();
				buffer.put(message.getData());
			}
		}
		byte[] temp = new byte[buffer.position()];
		System.arraycopy(data, 0, temp, 0, buffer.position());
		return temp;
	}
}