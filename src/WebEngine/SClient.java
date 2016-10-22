package WebEngine;
import java.net.InetAddress;
import java.util.UUID;

public class SClient {
	private InetAddress IPAddress;
	private int port;
	private UUID Id;
	private float ping;
	private ClientState state = ClientState.Init;
	
	public SClient(InetAddress IPAddress, int port){
		this.IPAddress = IPAddress;
		this.port = port;
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