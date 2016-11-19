package WebEngine;
import java.net.InetAddress;
import java.util.UUID;

public class SClient {
	private InetAddress IPAddress;
	private int port;
	private UUID Id;
	private float ping;
	private ClientState state = ClientState.Init;
	private String name;
	
	public SClient(InetAddress IPAddress, int port, UUID Id, String name){
		this.IPAddress = IPAddress;
		this.port = port;
		this.Id = Id;
		this.name = name;
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
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}