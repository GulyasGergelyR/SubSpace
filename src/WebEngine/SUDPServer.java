package WebEngine;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import GameEngine.Specifications;
import GameEngine.EntityEngine.SDistantHumanControl;
import GameEngine.EntityEngine.SEntity;
import Main.SMain;

public class SUDPServer {
	
	private Listener listener;
	private Handler handler;
	private List<SClient> clients;
	
	public SUDPServer(int port) throws Exception{
		DatagramSocket serverSocket = new DatagramSocket(port);
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
	
	public void Close(){
		listener.serverSocket.close();
		StopListener();
		StopHandler();
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
			byte[] receiveData = new byte[Specifications.DataLength];
            while(running){
            	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
					serverSocket.receive(receivePacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                String sentence = new String( receivePacket.getData());
                if(sentence.length()>0)
                	System.out.println("RECEIVED: " + sentence);
                
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                
                SMessage message = new SMessage(receivePacket.getData());
                
                if (message.isValid()){
                	boolean new_client = true;
                    for(SClient client : clients){
                    	if (client.getId().equals(message.getId())){
                    		new_client = false;
                    		break;
                    	}
                    }
                    if (new_client){
                    	// TODO add client side creation
                    	SClient client = new SClient(IPAddress, port, message.getId(), "Player");
                    	SEntity entity = new SEntity();
                    	entity.setController(new SDistantHumanControl(entity));
                    	entity.setId(message.getId());
                    	SMain.getGameInstance().addEntity(entity);
                    	client.setId(entity.getId());
                    	clients.add(client);
                    }
                    else{
                    	//System.out.println("RECEIVED: " + message.getId());
                        //System.out.println("RECEIVED: " + message.getCommand());
                        //System.out.println("RECEIVED: " + message.getContent());
                    	SMain.getGameInstance().AddClientMessage(new SMessage(receivePacket.getData()));
                    }
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
