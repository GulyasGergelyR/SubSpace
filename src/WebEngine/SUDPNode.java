package WebEngine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import GameEngine.Specifications;
import WebEngine.ComEngine.SCommunicationHandler;
import WebEngine.ComEngine.SCommunicationHandler.UDPNodeRole;
import WebEngine.ComEngine.SMessage;
import WebEngine.ComEngine.SNode;



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
	
	public void SendMessage(SMessage message, SNode node){
		byte[] sendData = message.createRawData();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, node.getIPAddress(), transmitPort);
	    try {
			transmitSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    /*
	    if(communicationHandler.getUDPNodeRole().equals(UDPNodeRole.Server))
	    	System.out.println("Data Sent from Server "+message.getContent());
	    if(communicationHandler.getUDPNodeRole().equals(UDPNodeRole.Client))
	    	System.out.println("Data Sent from Client "+message.getContent());
	    	*/
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
					if(communicationHandler.getUDPNodeRole().equals(UDPNodeRole.Server))
						System.out.println("Server listener is shutting down, because:\n\t"+e.getMessage());
					else if(communicationHandler.getUDPNodeRole().equals(UDPNodeRole.Client))
						System.out.println("Client listener is shutting down, because:\n\t"+e.getMessage());
					running = false;
					break;
				}
                //System.out.println("start parsing message...");
                communicationHandler.ParseMessageFromDatagramPacket(receivePacket);
                // TODO Remove junk below
               /* String sentence = new String( receivePacket.getData());
                if(sentence.length()>0)
                	System.out.println("RECEIVED: " + sentence);*/
            }
		}
	}
}
