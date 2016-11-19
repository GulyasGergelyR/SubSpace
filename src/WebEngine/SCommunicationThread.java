package WebEngine;

import java.net.DatagramSocket;

public abstract class SCommunicationThread extends Thread{
	protected boolean running = true;
	protected DatagramSocket socket;
	protected int port;
	
	public SCommunicationThread(DatagramSocket socket, int port){
		this.socket = socket;
		this.port = port;
	}
	
	public void StopThread(){
    	this.running = false;
    }
}
