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
	
	/*
    public static void main(String[] args) throws IOException {
        String serverAddress = JOptionPane.showInputDialog(
            "Enter IP Address of a machine that is\n" +
            "running the date service on port 9090:");
        Socket s = new Socket(serverAddress, 9090);
        BufferedReader input =
            new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        JOptionPane.showMessageDialog(null, answer);
        s.close();
        System.exit(0);
        
    }*/
}