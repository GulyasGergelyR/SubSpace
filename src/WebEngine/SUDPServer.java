package WebEngine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import GameEngine.EntityEngine.SEntity;
import Main.SMain;

public class SUDPServer {
	private Listener listener;
	private Handler handler;
	private List<SClient> clients;
	
	public SUDPServer(int port) throws Exception{
		DatagramSocket serverSocket = new DatagramSocket(9876);
        listener = new Listener(serverSocket);
        handler = new Handler(serverSocket);
        clients = new ArrayList<SClient>();
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
            while(running){
            	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
					serverSocket.receive(receivePacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                byte[] command = Arrays.copyOfRange(receivePacket.getData(), 0, 4);
                
                
                String sentence = new String( receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);
                
                
                
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                
                boolean new_client = true;
                for(SClient client : clients){
                	if (client.getIPAddress() == IPAddress){
                		new_client = false;
                	}
                }
                if (new_client){
                	// TODO add client side creation
                	SClient client = new SClient(IPAddress, port);
                	SEntity entity = new SEntity();
                	SMain.getGameInstance().addEntity(entity);
                	client.setId(entity.getId());
                }
                else{
                	
                }
                /*
                String capitalizedSentence = sentence.toUpperCase();
                sendData = capitalizedSentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                try {
					serverSocket.send(sendPacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
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
