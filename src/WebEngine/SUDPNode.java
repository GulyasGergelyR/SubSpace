package WebEngine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import GameEngine.Specifications;
import WebEngine.ComEngine.SCommunicationHandler;
import WebEngine.ComEngine.SCommunicationHandler.UDPRole;
import WebEngine.ComEngine.SNode;
import WebEngine.MessageEngine.SM;

public class SUDPNode {

	private Listener listener;
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
	
	public void SendMessage(SM message, SNode node){
		byte[] sendData = message.getData();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, node.getIPAddress(), transmitPort);
	    try {
			transmitSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    if(communicationHandler.getUDPRole().equals(UDPRole.Server))
	    	System.out.println("Data Sent from Server "+message.getData());
	    if(communicationHandler.getUDPRole().equals(UDPRole.Client))
	    	System.out.println("Data Sent from Client "+message.getData());
	    	
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
					if(communicationHandler.getUDPRole().equals(UDPRole.Server))
						System.out.println("Server listener is shutting down, because:\n\t"+e.getMessage());
					else if(communicationHandler.getUDPRole().equals(UDPRole.Client))
						System.out.println("Client listener is shutting down, because:\n\t"+e.getMessage());
					running = false;
					break;
				}
                communicationHandler.ParseMessageFromDatagramPacket(receivePacket);
                // TODO Remove junk below
               String sentence = new String(receivePacket.getData());
                if(sentence.length()>0)
                	System.out.println("RECEIVED: " + sentence);
            }
		}
	}
}
