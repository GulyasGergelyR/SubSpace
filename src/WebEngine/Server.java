package WebEngine;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * A TCP server that runs on port 9090.  When a client connects, it
 * sends the client the current date and time, then closes the
 * connection with that client.  Arguably just about the simplest
 * server you can write.
 */
public class Server {
	private int port;
	private ServerSocket listener;
	
	public Server(int port){
		this.port = port;
	}
	
	public void Listen() throws IOException{
		listener = new ServerSocket(port);
	}
	
	public void Read(){}
	
	public void Write(){}
}
