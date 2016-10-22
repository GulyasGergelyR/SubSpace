package WebEngine;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import Main.SMain;

public class SMessageHandler {
	private List<SClient> clients;
	private LinkedList<SMessage> ObjectMessages;
	private LinkedList<SMessage> EntityMessages;
	
	
	public SMessageHandler(){
		clients = new ArrayList<SClient>();
		ObjectMessages = new LinkedList<SMessage>();
		EntityMessages = new LinkedList<SMessage>();
	}
	public void ParseMessageFromByte(DatagramPacket receivePacket){
		byte[] input = receivePacket.getData();
		SMessage message = new SMessage(input);
		if(message.isValid()){
			//Check what type of message is it
			String command = message.getCommand();
			
			if (command.equals("CNNCL")){ //connect client
				ParseConnectCommand(receivePacket, message);
			}
			else if (command.equals("DSCCL")){ //connect client
				ParseDisConnectCommand(receivePacket, message);
			}
			else if (command.equals("PNGRQ")){ //connect client
				ParsePingRequest(message);
			}
		}
	}
	
	private void ParseConnectCommand(DatagramPacket receivePacket, SMessage message){
		SClient client = getClientById(message.getId());
		if(client==null){
			int length = Integer.parseInt(message.content.substring(0, 2));
			String name = message.content.substring(3,3+length);
			client = new SClient(receivePacket.getAddress(), receivePacket.getPort(),
					message.getId(), name);
			clients.add(client);
		}else{
			System.out.println("User already joined: "+client.getName());
		}
	}
	
	private void ParseDisConnectCommand(DatagramPacket receivePacket, SMessage message){
		SClient client = getClientById(message.getId());
		if(client==null){
			System.out.println("User who wants to disconnect was not found: "+message.getId());
		}else{
			clients.remove(client);
			SMain.getGameInstance().removeEntity(client.getId());
			//TODO create DEL message
		}
	}
	
	private void ParsePingRequest(SMessage message){
		
	}
	
	private SClient getClientById(UUID Id){
		for(SClient client : clients){
			if(client.getId().equals(Id)){
				return client;
			}
		}
		return null;
	}
	
	
}
