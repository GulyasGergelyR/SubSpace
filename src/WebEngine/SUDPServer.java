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
import WebEngine.ComEngine.SClient;
import WebEngine.ComEngine.SMessage;

public class SUDPServer {
	
	private Listener listener;
	private Handler handler;
	private List<SClient> clients;
	
	public SUDPServer(int port) throws Exception{
		DatagramSocket serverSocket = new DatagramSocket(port);
        listener = new Listener(serverSocket, port);
        handler = new Handler(serverSocket, port);
        clients = new ArrayList<SClient>();
        listener.start();
        handler.start();
	}
	
	protected void StopListener(){
		listener.StopThread();
	}
	protected void StopHandler(){
		handler.StopThread();
	}
	
	public void Close(){
		StopListener();
		StopHandler();
		listener.socket.close();
	}
	
	private class Handler extends SCommunicationThread{
		
		public Handler(DatagramSocket socket, int port){
			super(socket, port);
		}
		
	}
	
	private class Listener extends SCommunicationThread{
        
        public Listener(DatagramSocket socket, int port) {
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
					System.out.println("Receive failed, server shutting down");
					e.printStackTrace();
					running = false;
					break;
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
                    	SMain.getGameInstance().AddClientMessage(new SMessage(receivePacket.getData()));
                    }
                }
                
            }
		}
	}
	
	
}
