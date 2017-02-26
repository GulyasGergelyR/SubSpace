package WebEngine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import GameEngine.SId;
import GameEngine.Specifications;
import Main.SMain;
import WebEngine.ComEngine.SCommunicationHandler;
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
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, node.getAddress(), transmitPort);
	    try {
			transmitSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
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
					System.out.printf("%s listener is shutting down, because:\n\t"+e.getMessage()+"\n", SMain.getAppRole());
					System.out.println("Number of generated elements:"+SId.getNewId());
					running = false;
					break;
				}
                communicationHandler.ParseMessageFromDatagramPacket(receivePacket);
            }
		}
	}
}
