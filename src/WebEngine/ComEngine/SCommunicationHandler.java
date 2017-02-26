package WebEngine.ComEngine;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import GameEngine.SId;
import GameEngine.SPlayer.PlayerType;
import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.SyncEngine.SServerTimer;
import Main.SMain;
import WebEngine.SUDPNode;
import WebEngine.ComEngine.SNode.ConnectionState;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMParser;
import WebEngine.MessageEngine.SMPatterns;

public class SCommunicationHandler {
	private List<SNode> nodes;
	private SNode server;
	
	private SUDPNode udpNode;
	private SNode localNode;
	
	
	private LinkedList<SM> ObjectMessages;
	private LinkedList<SM> EntityMessages;
	private Object entitylock;
	private Object objectlock;
	
	private int requestPingDataCounter = 0;
	
	public SCommunicationHandler(){
		nodes = Collections.synchronizedList(new ArrayList<SNode>());
		ObjectMessages = new LinkedList<SM>();
		EntityMessages = new LinkedList<SM>();
		entitylock = new Object();
		objectlock = new Object();
	}
	
	public SNode getLocalNode(){
		return localNode;
	}
	public void setLocalNode(SNode localNode){
		this.localNode = localNode;
	}
	
	public SNode getNodeById(int Id){
		synchronized (nodes) {
			for(SNode node : nodes){
				if(node.equals(Id)){
					return node;
				}
			}
		}
		return null;
	}
	public SNode getNodeByAddress(InetAddress address){
		synchronized (nodes) {
			for(SNode node : nodes){
				if(node.getAddress().equals(address)){
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
	private void addEntityMessage(SM message){
		synchronized (entitylock) {
			EntityMessages.add(message);
		}
	}
	private void addObjectMessage(SM message){
		synchronized (objectlock) {
			ObjectMessages.add(message);
		}
	}
	public int getEntityMessageLength(){
		synchronized (entitylock) {
			return EntityMessages.size();
		}
	}
	public int getObjectMessageLength(){
		synchronized (objectlock) {
			return ObjectMessages.size();
		}
	}
	public SM popEntityMessage(){
		synchronized (entitylock) {
			return EntityMessages.pop();
		}
	}
	public SM popObjectMessage(){
		synchronized (objectlock) {
			return ObjectMessages.pop();
		}
	}
	
	@Deprecated
	public List<SM> getEntityMessagesForEntity(SEntity entity, int maxLength){
		synchronized (entitylock) {
			List<SM> messages = new ArrayList<SM>(5);
			int i = 0;
			for(SM message: EntityMessages){
				
				if(i<maxLength){
					if(true){
						messages.add(message);
					}
					i++;
				}else break;
				
			}
			EntityMessages.removeAll(messages);
			return messages;
		}
	}
	
	@Deprecated
	public List<SM> getEntityMessages(){
		synchronized (entitylock) {
			return EntityMessages;
		}
		
	}
	
	////////////////////////UDPNode\\\\\\\\\\\\\\\\\\\\\\
	
	public void CloseUDPNode(){
		if(localNode.getState().equals(ConnectionState.Connected)){
			DisconnectFromServer();
		}
		if (udpNode != null)
			udpNode.Close();
	}
	public void createUDPNode(int receivePort, int transmitPort){
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
		this.server = server;
		SM message = SMPatterns.getConnectToServerMessage(localNode.getName());
		udpNode.SendMessage(message, server);
	}
	public void DisconnectFromServer(){
		localNode.setState(ConnectionState.NotConnected);
		SM message = SMPatterns.getDisconnectFromServerMessage(localNode);
		udpNode.SendMessage(message, server);
	}
	
	/////////////////////////////Ping\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	public void RequestPingDataFromClients(){
		
		if (requestPingDataCounter<60){
			requestPingDataCounter++;
			return;
		} else{
			requestPingDataCounter = 0;
		}
		synchronized (nodes) {
			for(SNode node : nodes){
				RequestPingDataFromClient(node);
			}
		}
	}
	private void RequestPingDataFromClient(SNode client){
		SM message = SMPatterns.getPingRequestMessage(client, SServerTimer.GetNanoTime());
		udpNode.SendMessage(message, client);
	}
	
	////////////////////////////Message\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	public void SendMessage(SM message){
		if(!SMain.IsServer()){
			if (localNode.getState().equals(ConnectionState.Connected))
				udpNode.SendMessage(message, server);
		} else{
			synchronized (nodes) {
				for(SNode node : nodes){
					udpNode.SendMessage(message, node);
				}
			}
		}
	}
	public void SendMessageToNode(SM message, int id){
		synchronized (nodes){
			SNode node = getNodeById(id);
			if(!node.equals(localNode))
				udpNode.SendMessage(message, node);
			else{
				System.out.println("Trying to send to itself");
			}
		}
	}
	public void SendMessageToNode(SM message, SNode node){
		synchronized (nodes) {
			if(!node.equals(localNode))
				udpNode.SendMessage(message, node);
			else{
				System.out.println("Trying to send to itself");
			}
		}
	}
	public void SendMessageExceptToNode(SM message, SNode notNode){
		synchronized (nodes) {
			for(SNode node : nodes){
				if(!node.equals(notNode))
					udpNode.SendMessage(message, node);
			}
		}
	}
	
	/////////////////////////////Parsing\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	public void ParseMessageFromDatagramPacket(DatagramPacket receivePacket){
		SM message = new SM(receivePacket);
		//System.out.println("parsing message...");
		if(message.isValid()){
			byte command = message.getCommandId();
			//System.out.println("Received command: "+String.format("%02x", command & 0xff));
			////////////////////////SERVER\\\\\\\\\\\\\\\\\\\\\\
			if(SMain.IsServer()){
				if (command == SMPatterns.CConnect){ 		//connect client
					ParseConnectCommand(message);
				}
				else if (command == SMPatterns.CDisconnect){ 	//disconnect client
					ParseDisconnectCommand(message);
				}
				else if (command == SMPatterns.CPingAnswer){ 	//ping answer from client
					ParsePingAnswer(message);
				}
				else if (command == SMPatterns.CClientInput){ 	//Client input (pressed key, mouse moved, mouse click)
					addEntityMessage(message);
				} 
				else if (command == SMPatterns.CObjectRequestCreate){ 	//Client does not know have an object
					addObjectMessage(message);
				}
				else{
					System.out.println("Server received unknown message: "+String.format("%02x", command & 0xff));
				}
			}
			////////////////////////CLIENT\\\\\\\\\\\\\\\\\\\\\\
			else{
				if (command == SMPatterns.CConnectNotAllowed){ 		//connection is not allowed
					ParseConnectNotAllowedCommand(message);
				}
				else if (command == SMPatterns.CConnectAllowed){ 	//connection is approved
					ParseConnectAllowedCommand(message);
				}
				else if (command == SMPatterns.CPingRequest){ 	//ping request from server
					ParsePingRequest(message);
				}
				else if (command == SMPatterns.CEntityUpdate){ 	//Server updates Entity information
					addEntityMessage(message);
				}
				else if (command == SMPatterns.CEntityUpdateState){ 	//Server updates Entity information
					addEntityMessage(message);
				}
				else if (command == SMPatterns.CEntityCreate){ 	//Server creates Entity
					addEntityMessage(message);
				}
				else if (command == SMPatterns.CEntityDelete){ 	//Server deletes an Entity
					addEntityMessage(message);
				}
				else if (command == SMPatterns.CObjectCreate){ 	//Server created Object
					addObjectMessage(message);
				}
				else if (command == SMPatterns.CObjectUpdate){ 	//Server updates Object information
					addObjectMessage(message);
				}
				else if (command == SMPatterns.CObjectDelete){ 	//Server deleted Object
					addObjectMessage(message);
				}
				else if (command == SMPatterns.CAnimationObjectCreate){ 	//Server created explosion
					addObjectMessage(message);
				}
				else{
					System.out.println("Received unknown message: "+String.format("%02x", command & 0xff));
				}
			}
		} else{
			//TODO Send back error
			System.out.println("Client received invalid message");
		}
	}
	
	private void ParseConnectCommand(SM message){
		ByteBuffer buffer = message.getBuffer();
		SNode client = getNodeByAddress(message.getAddress());
		if(client==null){
			byte nameLength = buffer.get();
			byte[] nameBytes = new byte[nameLength];
			for (int i=0; i<nameLength;i++){
				nameBytes[i] = buffer.get();
			}
			String name = new String(nameBytes);
			if(name.length() == 0){
				//TODO add proper name check
				System.out.println("Client tried to join with invalid name "+message.getAddress().toString());
				return;
			}
			else{
				client = new SNode(message.getAddress(), message.getPort(), name, PlayerType.lan);
				synchronized (nodes) {
					nodes.add(client);
					System.out.println("Client added: "+client.getName());
				}
				//TODO add normal entity creation
				SEntity entity = new SEntity(client.getPlayer());
				SMain.getGameInstance().addEntity(entity);
				SMain.getGameInstance().addPlayer(client.getPlayer());
				SM connectallowed = SMPatterns.getConnectAllowedMessage(client);
				udpNode.SendMessage(connectallowed, client);
				//RequestPingDataFromClient(client); TODO decide if ping request needed here
				
				//TODO add position initialization
				//Send to other clients
				SM createEntity = SMPatterns.getEntityCreateMessage(client.getPlayer());
				SendMessage(createEntity);
			}
		}else{
			System.out.println("Client already joined: "+client.getName());
		}
	}
	
	private void ParseDisconnectCommand(SM message){
		SNode client = getNodeByAddress(message.getAddress());
		if(client==null){
			System.out.println("Client who wants to disconnect was not found "+message.getAddress().toString());
		}else{
			Thread deleteNodeThread = new Thread(){
				@Override
				public void run(){
					client.getPlayer().getEntity().setObjectState(ObjectState.WaitingDelete);
					SM message = SMPatterns.getEntityDeleteMessage(client.getPlayer().getEntity());
					SendMessage(message);
					synchronized (nodes) {
						nodes.remove(client);
					}
					System.out.println("Client removed: "+client.getName());
				}
			};
			deleteNodeThread.start();
		}
	}
	private void ParseConnectAllowedCommand(SM message){
		localNode.setState(ConnectionState.Connected);
		ByteBuffer buffer = message.getBuffer();
		int id = SMParser.parseId(buffer);
		localNode.setId(new SId(id));
	}
	private void ParseConnectNotAllowedCommand(SM message){
		// TODO handle not allowed connection
		System.out.println("Connection is not allowed");
	}
	
	private void ParsePingRequest(SM message){
		ByteBuffer buffer = message.getBuffer();
		long nanoTime = buffer.getLong();
		float ping = buffer.getShort();
		localNode.setPing(ping);
		//Send back the same message to the server
		SM answer = SMPatterns.getPingAnswerMessage(nanoTime);
		udpNode.SendMessage(answer, server);
	}
	
	private void ParsePingAnswer(SM message){
		SNode client = getNodeByAddress(message.getAddress());
		if (client!=null){
			ByteBuffer buffer = message.getBuffer();
			long nanoTime = buffer.getLong();
			client.setPing((SServerTimer.GetNanoTime()-nanoTime)/1000/1000);
		}
	}
	
	
	
}
