package WebEngine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.sound.midi.Transmitter;

import org.lwjgl.opengl.Display;
import org.w3c.dom.css.Counter;

import GameEngine.SId;
import GameEngine.Specifications;
import GameEngine.ObjectEngine.SFH;
import GameEngine.SyncEngine.SServerTimer;
import Main.SMain;
import WebEngine.ComEngine.SCommunicationHandler;
import WebEngine.ComEngine.SNode;
import WebEngine.MessageEngine.SM;

public class SUDPNode {
	
	private Transmitter transmitter;
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
        transmitter = new Transmitter(transmitSocket, transmitPort);
        transmitter.start();
	}
	
	public void Close(){
		listener.socket.close();
		StopListener();
		transmitter.socket.close();
		StopTransmitter();
	}
	
	protected void StopListener(){
		listener.StopThread();
	}
	
	protected void StopTransmitter(){
		listener.StopThread();
	}
	
	@Deprecated
	public void SendMessage(SM message, SNode node){
		byte[] sendData = message.getData();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, node.getAddress(), transmitPort);
	    try {
			transmitSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void flushNode(SNode node){
		if (node.hasMessages()){
			byte[] sendData = node.flushMessages();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, node.getAddress(), transmitPort);
		    try {
				transmitSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class Listener extends SCommunicationThread{
		public Listener(DatagramSocket socket, int port){
			super(socket, port);
		}
		
		@Override
		public void run() {
            while(running){
            	byte[] receiveData = new byte[Specifications.MessageLength];
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
	
	private class Transmitter extends SCommunicationThread{
		public Transmitter(DatagramSocket socket, int port){
			super(socket, port);
		}
		
		@Override
		public void run() {
			SServerTimer timer = new SServerTimer();
			timer.StartTimer();	
			int counter = 0;
            while(running){
            	if (counter ==0){
            		communicationHandler.SendMessagesToNodes();
            		counter++;
            	}else{
            		counter = 0;
            	}
                timer.SleepIfRequired();
            }
		}
	}
}
