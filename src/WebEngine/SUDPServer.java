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
import WebEngine.ComEngine.SNode;
import WebEngine.ComEngine.SMessage;

public class SUDPServer {
	
	private Listener listener;
	private Handler handler;
	private List<SNode> clients;
	
	public SUDPServer(int receivePort, int transmitPort) throws Exception{
		DatagramSocket clientSocket = new DatagramSocket();
        handler = new Handler(clientSocket, transmitPort);
        DatagramSocket serverSocket = new DatagramSocket(receivePort);
        listener = new Listener(serverSocket, receivePort);
        listener.start();
        
        clients = new ArrayList<SNode>();
	}
	
	protected void StopListener(){
		listener.StopThread();
	}
	
	public void Close(){
		listener.socket.close();
		StopListener();
	}
	
	public void SendMessage(SMessage message, SNode c) throws Exception{
		if(c == null){ // It is a broadcast
			for(SNode client : SMain.getCommunicationHandler().getClients()){
				handler.SendMessage(client, message);
			}
		}
		else 
			handler.SendMessage(c, message);
	}
	
	private class Handler {
		protected DatagramSocket socket;
		protected int port;
		
		public Handler(DatagramSocket socket, int port) throws Exception{
			this.socket = socket;
			this.port = port;
		}
		public void SendMessage(SNode client, SMessage message) throws Exception{
			
			byte[] sendData = message.createRawData();
			
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client.getIPAddress(), port);
		    socket.send(sendPacket);
		    System.out.println("Data Sent "+message.getContent());
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
                SMain.getCommunicationHandler().ParseMessageFromDatagramPacket(receivePacket);
                
                ///////////////// TODO Remove junk below
                String sentence = new String( receivePacket.getData());
                if(sentence.length()>0)
                	System.out.println("RECEIVED: " + sentence);
                
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                
                SMessage message = new SMessage(receivePacket.getData());

                if (message.isValid()){
                	boolean new_client = true;
                    for(SNode client : clients){
                    	if (client.getId().equals(message.getId())){
                    		new_client = false;
                    		break;
                    	}
                    }
                    if (new_client){
                    	// TODO add client side creation
                    	SNode client = new SNode(IPAddress, port, message.getId(), "Player");
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
