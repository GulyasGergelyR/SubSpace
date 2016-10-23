package WebEngine;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import WebEngine.ComEngine.SMessage;



public class SUDPClient {

	private Listener listener;
	private Handler handler;
	private InetAddress ServerAddress;
	
	public SUDPClient(int port) throws Exception{
		DatagramSocket clientSocket = new DatagramSocket();
        handler = new Handler(clientSocket, port);
        listener = new Listener(clientSocket, port);
        listener.start();
	}
	
	public void Close(){
		StopListener();
		listener.socket.close();
	}
	
	protected void StopListener(){
		listener.StopThread();
	}
	
	public void SendMessage(SMessage message) throws Exception{
		handler.SendMessage(message);
	}
	
	
	private class Handler {
		protected DatagramSocket clientSocket;
		protected int port;
		public Handler(DatagramSocket socket, int port) throws Exception{
			this.clientSocket = socket;
			this.port = port;
			ServerAddress = InetAddress.getByName("localhost");
		}
		public void SendMessage(SMessage message) throws Exception{
			
			byte[] sendData = message.getData();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ServerAddress, port);
		    clientSocket.send(sendPacket);
		    System.out.println("Data Sent "+message.getContent());
		}
	}
	
	private class Listener extends SCommunicationThread{
		public Listener(DatagramSocket socket, int port){
			super(socket, port);
		}
	}
}
