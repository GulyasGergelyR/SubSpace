package WebEngine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import GameEngine.Specifications;
import WebEngine.ComEngine.SMessage;



public class SUDPClient {

	private Listener listener;
	private Handler handler;
	private InetAddress ServerAddress;
	
	public SUDPClient(int receivePort, int transmitPort) throws Exception{
		DatagramSocket clientSocket = new DatagramSocket();
        handler = new Handler(clientSocket, transmitPort);
        DatagramSocket serverSocket = new DatagramSocket(receivePort);
        listener = new Listener(serverSocket, receivePort);
        listener.start();
	}
	
	public void Close(){
		listener.socket.close();
		StopListener();
	}
	
	protected void StopListener(){
		listener.StopThread();
	}
	
	public void SendMessage(SMessage message) throws Exception{
		handler.SendMessage(message);
	}
	
	private class Handler {
		protected DatagramSocket socket;
		protected int port;
		
		public Handler(DatagramSocket socket, int port) throws Exception{
			this.socket = socket;
			this.port = port;
			ServerAddress = InetAddress.getByName("localhost");
		}
		public void SendMessage(SMessage message) throws Exception{
			
			byte[] sendData = message.getData();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ServerAddress, port);
		    socket.send(sendPacket);
		    System.out.println("Data Sent "+message.getContent());
		}
	}
	
	private class Listener extends SCommunicationThread{
		public Listener(DatagramSocket socket, int port){
			super(socket, port);
		}
		
		@Override
		public void run() {
			byte[] receiveData = new byte[Specifications.DataLength];
            while(running){
            	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
					socket.receive(receivePacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("Receive failed, client shutting down");
					//e.printStackTrace();
					running = false;
					break;
				}
                
                String sentence = new String( receivePacket.getData());
                if(sentence.length()>0)
                	System.out.println("RECEIVED: " + sentence);
            }
		}
	}
}
