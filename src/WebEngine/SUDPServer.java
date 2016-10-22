package WebEngine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SUDPServer {
	private Listener listener;
	private Handler handler;
	
	public SUDPServer(int port) throws Exception{
		DatagramSocket serverSocket = new DatagramSocket(9876);
        listener = new Listener(serverSocket);
        handler = new Handler(serverSocket);
        listener.start();
        handler.start();
        
	}
	
	public void StopListener(){
		listener.StopThread();
	}
	public void StopHandler(){
		handler.StopThread();
	}
	
	private class Handler extends CommunicationThread{
		
		public Handler(DatagramSocket socket){
			super(socket);
		}
		
	}
	
	private class Listener extends CommunicationThread{
        
        
        public Listener(DatagramSocket socket) {
            super(socket);
        }

		@Override
		public void run() {
			byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            while(running){
            	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
					serverSocket.receive(receivePacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                String sentence = new String( receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);
                
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length, IPAddress, port);
                try {
					serverSocket.send(sendPacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }
		}
        
        
	}
	
	private abstract class CommunicationThread extends Thread{
		protected boolean running = true;
		protected DatagramSocket serverSocket;
		
		public CommunicationThread(DatagramSocket socket){
			serverSocket = socket;
		}
		
		public void StopThread(){
        	this.running = false;
        }
	}
}
