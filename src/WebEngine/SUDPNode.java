package WebEngine;

import java.net.DatagramSocket;

public abstract class SUDPNode {

	protected Listener listener;
	protected Handler handler;
	protected DatagramSocket socket;
	protected int port;
	
	private class Handler extends CommunicationThread{
		
		public Handler(DatagramSocket socket, int port){
			super(socket, port);
		}
		
	}
	
	private class Listener extends CommunicationThread{
		public Listener(DatagramSocket socket, int port){
			super(socket,port);
		}
	}
	
	private abstract class CommunicationThread extends Thread{
		protected boolean running = true;
		protected DatagramSocket socket;
		protected int port;
		
		public CommunicationThread(DatagramSocket socket, int port){
			this.socket = socket;
			this.port = port;
		}
		
		public void StopThread(){
        	this.running = false;
        }
	}
}
