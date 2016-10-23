package WebEngine.ComEngine;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import GameEngine.Specifications;
import GameEngine.SyncEngine.SServerTimer;
import Main.SMain;
import WebEngine.SUDPClient;

public class SCommunicationHandler {
	private List<SClient> clients;
	private LinkedList<SMessage> ObjectMessages;
	private LinkedList<SMessage> EntityMessages;
	
	public SCommunicationHandler(){
		clients = new ArrayList<SClient>();
		ObjectMessages = new LinkedList<SMessage>();
		EntityMessages = new LinkedList<SMessage>();
	}
	public void ParseMessageFromDatagramPacket(DatagramPacket receivePacket){
		byte[] input = receivePacket.getData();
		SMessage message = new SMessage(input);
		if(message.isValid()){
			//Check what type of message is it
			String command = message.getCommandName();
			
			if (command.equals("CNNCL")){ //connect client
				ParseConnectCommand(receivePacket, message);
			}
			else if (command.equals("DSCCL")){ //connect client
				ParseDisconnectCommand(receivePacket, message);
			}
			else if (command.equals("PNGRQ")){ //ping request from server
				ParsePingRequest(message);
			}
			else if (command.equals("PNGAN")){ //ping answer from client
				ParsePingAnswer(message);
			}
			else{
				for (String s : Specifications.EntityCommands){
					if(command.equals(s)){
						EntityMessages.add(message);
						return;
					}
				}
				for (String s : Specifications.ObjectCommands){
					if(command.equals(s)){
						ObjectMessages.add(message);
						return;
					}
				}
			System.out.println("Unknown commad: "+command);
			}
		} else{
			System.out.println("Received invalid message: \n\t"+new String(input));
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
	
	private void ParseDisconnectCommand(DatagramPacket receivePacket, SMessage message){
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
		SUDPClient client = SMain.getUDPClient();
		if (client!=null){
			//TODO add previous ping parsing
			message.setCommandName("PNGAN");
			try {
				client.SendMessage(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void ParsePingAnswer(SMessage message){
		SClient client = getClientById(message.getId());
		if (client!=null){
			int length = Integer.parseInt(message.content.substring(0, 2));
			String nanoTimeS = message.content.substring(3,3+length);
			long nanoTime = Long.parseUnsignedLong(nanoTimeS);
			client.setPing(SServerTimer.GetNanoTime()-nanoTime);
		}
	}
	
	private SClient getClientById(UUID Id){
		for(SClient client : clients){
			if(client.getId().equals(Id)){
				return client;
			}
		}
		return null;
	}
	
	public int getEntityMessageLength(){
		return EntityMessages.size();
	}
	public int getObjectMessageLength(){
		return ObjectMessages.size();
	}
	public SMessage popEntityMessage(){
		return EntityMessages.pop();
	}
	public SMessage popObjectMessage(){
		return ObjectMessages.pop();
	}
	
	public List<SClient> getClients(){
		return clients;
	}
	
}
