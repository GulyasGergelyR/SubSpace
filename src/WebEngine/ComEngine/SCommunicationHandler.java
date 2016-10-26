package WebEngine.ComEngine;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import GameEngine.EntityEngine.SHumanControl;
import GameEngine.EntityEngine.SEntity;
import GameEngine.SyncEngine.SServerTimer;
import Main.SMain;
import WebEngine.SUDPNode;
import WebEngine.ComEngine.SNode.NodeState;

public class SCommunicationHandler {
	private List<SNode> nodes;
	private SNode server;
	
	private SUDPNode udpNode;
	private SNode localNode;
	
	private UDPNodeRole udpNodeRole;
	public enum UDPNodeRole{
		Server, Client 
	}
	
	private LinkedList<SMessage> ObjectMessages;
	private LinkedList<SMessage> EntityMessages;
	private Object entitylock;
	
	public SCommunicationHandler(){
		nodes = Collections.synchronizedList(new ArrayList<SNode>());
		ObjectMessages = new LinkedList<SMessage>();
		EntityMessages = new LinkedList<SMessage>();
	}
	
	public SNode getLocalNode(){
		return localNode;
	}
	public void setLocalNode(SNode localNode){
		this.localNode = localNode;
	}
	
	public SNode getNodeById(UUID Id){
		synchronized (nodes) {
			for(SNode node : nodes){
				if(node.getId().equals(Id)){
					return node;
				}
			}
		}
		return null;
	}
	public List<SNode> getNodes(){
		synchronized (nodes){
			return nodes;
		}
	}
	private void addEntityMessage(SMessage message){
		synchronized (entitylock) {
			EntityMessages.add(message);
		}
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
	public List<SMessage> getEntityMessagesForEntity(SEntity entity, int maxLength){
		synchronized (entitylock) {
			List<SMessage> messages = new ArrayList<SMessage>(5);
			int i = 0;
			for(SMessage message: EntityMessages){
				if(i<maxLength){
					if(message.getId().equals(entity.getId())){
						messages.add(message);
					}
					i++;
				}else break;
				
			}
			EntityMessages.removeAll(messages);
			return messages;
		}
		
	}
	
	////////////////////////UDPNode\\\\\\\\\\\\\\\\\\\\\\
	public UDPNodeRole getUDPNodeRole(){
		return udpNodeRole;
	}
	
	public void CloseUDPNode(){
		if(localNode.getState().equals(NodeState.Connected)){
			DisconnectFromServer();
		}
		if (udpNode != null)
			udpNode.Close();
	}
	public void createUDPNodeAsClient(int receivePort, int transmitPort){
		udpNodeRole = UDPNodeRole.Client;
		createUDPNode(receivePort, transmitPort);
	}
	public void createUDPNodeAsServer(int receivePort, int transmitPort){
		udpNodeRole = UDPNodeRole.Server;
		createUDPNode(receivePort, transmitPort);
	}
	private void createUDPNode(int receivePort, int transmitPort){
		if(receivePort==transmitPort){
			System.out.println("Ports must be different");
			return;
		}
		if(udpNode != null){
			udpNode.Close();
		}
		try {
			udpNode = new SUDPNode(this, receivePort, transmitPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	////////////////////////Connect-Disconnect\\\\\\\\\\\\\\\\\\\\\\
	public void ConnectToServer(SNode server){
		udpNodeRole = UDPNodeRole.Client;
		this.server = server;
		SMessage message = new SMessage(localNode.getId(), "CNNCL", "");
		message.addContent(localNode.getName());
		udpNode.SendMessage(message, server);
	}
	public void DisconnectFromServer(){
		localNode.setState(NodeState.NotConnected);
		SMessage message = new SMessage(localNode.getId(), "DSCCL", "");
		udpNode.SendMessage(message, server);
	}
	
	/////////////////////////////Ping\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	public void RequestPingDataFromClients(){
		synchronized (nodes) {
			for(SNode node : nodes){
				RequestPingDataFromClient(node);
			}
		}
	}
	private void RequestPingDataFromClient(SNode client){
		SMessage message = new SMessage(client.getId(), "PNGRQ", "");
		message.addContent(Long.toUnsignedString(SServerTimer.GetNanoTime()));
		if (client.getPing()>999){
			message.addContent("999");
		}else{
			message.addContent(Integer.toUnsignedString((int)client.getPing()));
		}
		udpNode.SendMessage(message, client);
	}
	
	////////////////////////////Message\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	public void SendMessage(SMessage message){
		if(udpNodeRole.equals(UDPNodeRole.Client)){
			if (localNode.getState().equals(NodeState.Connected))
				udpNode.SendMessage(message, server);
		}
		else if(udpNodeRole.equals(UDPNodeRole.Server)){
			synchronized (nodes) {
				for(SNode node : nodes){
					udpNode.SendMessage(message, node);
				}
			}
		}
	}
	public void SendMessageToNode(SMessage message, SNode node){
		if(!node.equals(localNode))
			udpNode.SendMessage(message, node);
		else{
			System.out.println("Trying to send to itself");
		}
	}
	
	/////////////////////////////Parsing\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	public void ParseMessageFromDatagramPacket(DatagramPacket receivePacket){
		byte[] input = receivePacket.getData();
		SMessage message = new SMessage(input);
		//System.out.println("parsing message...");
		if(message.isValid()){
			String command = message.getCommandName();
			
			////////////////////////SERVER\\\\\\\\\\\\\\\\\\\\\\
			if(udpNodeRole.equals(UDPNodeRole.Server)){
				if (command.equals("CNNCL")){ 		//connect client
					ParseConnectCommand(receivePacket, message);
				}
				else if (command.equals("DSCCL")){ 	//disconnect client
					ParseDisconnectCommand(message);
				}
				else if (command.equals("PNGAN")){ 	//ping answer from client
					ParsePingAnswer(message);
				}
				else if (command.equals("CLIIN")){ 	//Client input (pressed key, mouse click)
					addEntityMessage(message);
				}
				else{
					System.out.println("Server received unknown message: \n\t"+new String(input));
				}
				
			}
			////////////////////////CLIENT\\\\\\\\\\\\\\\\\\\\\\
			else if(udpNodeRole.equals(UDPNodeRole.Client)){
				if (command.equals("CNNNA")){ 		//connection is not allowed
					ParseConnectNotAllowedCommand(message);
				}
				else if (command.equals("CNNAP")){ 	//connection is approved
					ParseConnectApprovedCommand(message);
				}
				else if (command.equals("PNGRQ")){ 	//ping request from server
					ParsePingRequest(message);
				}
				else if (command.equals("ENTUP")){ 	//Server updates Entity information
					addEntityMessage(message);
				}
				else if (command.equals("ENTDE")){ 	//Server deleted an Entity
					addEntityMessage(message);
				}
				else if (command.equals("OBJCR")){ 	//Server created Object
					ObjectMessages.add(message);
				}
				else if (command.equals("OBJUP")){ 	//Server updates Object information
					ObjectMessages.add(message);
				}
				else if (command.equals("OBJDE")){ 	//Server deleted Object
					ObjectMessages.add(message);
				}
				else{
					System.out.println("Received unknown message: \n\t"+new String(input));
				}
			}
		} else{
			//TODO Send back error
			System.out.println("Client received invalid message: \n\t"+new String(input));
		}
	}
	
	private void ParseConnectCommand(DatagramPacket receivePacket, SMessage message){
		SNode client = getNodeById(message.getId());
		if(client==null){
			String name = SMessagePatterns.getConnectCommandName(message);
			if(name == null){
				System.out.println("Client tried to join with invalid name: "+message.getContent());
				return;
			}
			else{
				client = new SNode(receivePacket.getAddress(), receivePacket.getPort(),
						message.getId(), name);
				synchronized (nodes) {
					nodes.add(client);
					System.out.println("Client added: "+client.getName());
				}
				//TODO add normal entity creation
				SEntity entity = new SEntity();
            	entity.setController(new SHumanControl(entity));
            	entity.setId(client.getId());
				client.getPlayer().setEntity(entity);
				SMain.getGameInstance().addEntity(entity);
				SMessage connectallowed = new SMessage(client.getId(),"CNNAP","");
				udpNode.SendMessage(connectallowed, client);
				RequestPingDataFromClient(client);
			}
		}else{
			System.out.println("Client already joined: "+client.getName());
		}
	}
	
	private void ParseDisconnectCommand(SMessage message){
		SNode client = getNodeById(message.getId());
		if(client==null){
			System.out.println("Client who wants to disconnect was not found: "+message.getId());
		}else{
			Thread deleteNodeThread = new Thread(){
				@Override
				public void run() {
					synchronized (nodes) {
						nodes.remove(client);
						System.out.println("Client removed: "+client.getName());
					}
					//TODO look here if there is an entity nullpointer error
					SMain.getGameInstance().removeEntity(client.getId());
					SMessage deleteentity = new SMessage(client.getId(),"DELEN","");
					SendMessage(deleteentity);
				}
			};
			deleteNodeThread.start();
		}
	}
	private void ParseConnectApprovedCommand(SMessage message){
		localNode.setState(NodeState.Connected);
	}
	private void ParseConnectNotAllowedCommand(SMessage message){
		// TODO handle not allowed connection
		System.out.println("Connection is not allowed");
	}
	
	private void ParsePingRequest(SMessage message){
		String ping = SMessagePatterns.getPingCommandPrevPing(message);
		localNode.setPing(Float.parseFloat(ping));
		//Send back the same message to the server
		message.setCommandName("PNGAN");
		udpNode.SendMessage(message, server);
	}
	
	private void ParsePingAnswer(SMessage message){
		SNode client = getNodeById(message.getId());
		if (client!=null){
			String time = SMessagePatterns.getPingCommandTime(message);
			long nanoTime = Long.parseUnsignedLong(time);
			long ping = SServerTimer.GetNanoTime()-nanoTime;
			client.setPing((SServerTimer.GetNanoTime()-nanoTime)/1000/1000);
			//System.out.println("ping: "+ping/1000/1000);
		}
	}
	
	
	
}
