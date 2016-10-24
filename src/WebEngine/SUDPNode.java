package WebEngine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import GameEngine.Specifications;
import WebEngine.ComEngine.SCommunicationHandler;
import WebEngine.ComEngine.SNode;
import WebEngine.ComEngine.SMessage;



public class SUDPNode {

	private Listener listener;
	private InetAddress ServerAddress;
	private DatagramSocket transmitSocket;
	private int transmitPort;
	private SCommunicationHandler communicationHandler;
	
	public SUDPNode(SCommunicationHandler communicationHandler, int receivePort, int transmitPort) throws Exception{
		this.communicationHandler = communicationHandler;
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
	
	public void SendMessage(SMessage message, SNode node){
		byte[] sendData = message.createRawData();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, node.getIPAddress(), transmitPort);
	    try {
			transmitSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
					System.out.println("Receive failed, node shutting down");
					running = false;
					break;
				}
                System.out.println("start parsing message...");
                communicationHandler.ParseMessageFromDatagramPacket(receivePacket);
                // TODO Remove junk below
                String sentence = new String( receivePacket.getData());
                if(sentence.length()>0)
                	System.out.println("RECEIVED: " + sentence);
            }
		}
	}
}
