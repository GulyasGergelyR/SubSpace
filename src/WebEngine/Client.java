package WebEngine;
import java.io.IOException;
import java.net.Socket;

/**
 * Trivial client for the date server.
 */
public class Client {
	private String serverAddress;
	private int port;
	private Socket socket;
	
	public Client(String serverAddress, int port){
		this.serverAddress = serverAddress;
		this.port = port;
	}
	
	public void Connect() throws IOException{
		socket = new Socket(serverAddress, port);
	}
	
	public void Read(){}
	
	public void Write(){}
}