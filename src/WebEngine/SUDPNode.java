package WebEngine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import GameEngine.Specifications;
import WebEngine.ComEngine.SNode;
import WebEngine.ComEngine.SMessage;



public class SUDPNode {

	private Listener listener;
	private InetAddress ServerAddress;
	private DatagramSocket transmitSocket;
	private int transmitPort;
	
	public SUDPNode(int receivePort, int transmitPort) throws Exception{
		this.transmitSocket = new DatagramSocket();
        this.transmitPort = transmitPort;
        DatagramSocket receiveSocket = new DatagramSocket(receivePort);
        listener = new Listener(receiveSocket, receivePort);
        listener.start();
	}
	
	public void Close(){
		listener.socket.close();
		StopListener();
	}
	
	protected void StopListener(){
		listener.StopThread();
	}
	
	public void SendMessage(SMessage message, SNode node) throws Exception{
		byte[] sendData = message.createRawData();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, node.getIPAddress(), transmitPort);
	    transmitSocket.send(sendPacket);
	    System.out.println("Data Sent "+message.getContent());
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
