package WebEngine.ComEngine;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import GameEngine.SPlayer;
import GameEngine.EntityEngine.SDistantHumanControl;
import GameEngine.EntityEngine.SEntity;
import GameEngine.SyncEngine.SServerTimer;
import Main.SMain;
import WebEngine.SUDPClient;
import WebEngine.SUDPNode;
import WebEngine.ComEngine.SNode.NodeState;

public class SCommunicationHandler {
	private List<SNode> nodes;
	private SNode server;
	
	private SUDPNode udpNode;
	private SNode localNode;
	private UDPNodeRole udpNodeRole;
	
	private LinkedList<DatagramPacket> MessagePipeLine;
	
	private LinkedList<SMessage> ObjectMessages;
	private LinkedList<SMessage> EntityMessages;
	
	public SCommunicationHandler(){
		nodes = Collections.synchronizedList(new ArrayList<SNode>());
		ObjectMessages = new LinkedList<SMessage>();
		EntityMessages = new LinkedList<SMessage>();
	}
	public enum UDPNodeRole{
		Server, Client 
	}
	
	public void CloseUDPNode(){
		if(localNode.getState().equals(NodeState.Connected)){
			DisconnectFromServer();
		}
		if (udpNode != null)
			udpNode.Close();
	}
	
	public SNode getLocalNode(){
		return localNode;
	}
	public void setLocalNode(SNode localNode){
		this.localNode = localNode;
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
		if(udpNode != null){
			udpNode.Close();
		}
		try {
			udpNode = new SUDPNode(this, receivePort, transmitPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
	
	
	public void ParseMessageFromDatagramPacket(DatagramPacket receivePacket){
		byte[] input = receivePacket.getData();
		SMessage message = new SMessage(input);
		System.out.println("parsing message...");
		if(message.isValid()){
			String command = message.getCommandName();
			
			////////////////////////SERVER\\\\\\\\\\\\\\\\\\\\\\
			if(udpNodeRole.equals(UDPNodeRole.Server)){
				if (command.equals("CNNCL")){ //connect client
					ParseConnectCommand(receivePacket, message);
				}
				else if (command.equals("DSCCL")){ //disconnect client
					ParseDisconnectCommand(message);
				}
				else if (command.equals("PNGAN")){ //ping answer from client
					ParsePingAnswer(message);
				}
			}
			////////////////////////CLIENT\\\\\\\\\\\\\\\\\\\\\\
			else if(udpNodeRole.equals(UDPNodeRole.Client)){
				if (command.equals("CNNNA")){ //connect not allowed
					ParseConnectNotAllowedCommand(message);
				}
				else if (command.equals("CNNAP")){ //connect approved
					ParseConnectApprovedCommand(message);
				}
				else if (command.equals("PNGRQ")){ //ping request from server
					ParsePingRequest(message);
				}
			}
		} else{
			//TODO Send back error
			System.out.println("Received invalid message: \n\t"+new String(input));
		}
	}
	
	private void ParseConnectCommand(DatagramPacket receivePacket, SMessage message){
		SNode client = getNodeById(message.getId());
		if(client==null){
			String name = SMessageParser.getConnectCommandName(message);
			if(name == null){
				System.out.println("User tried to join with invalid name: "+message.getContent());
				return;
			}
			else{
				client = new SNode(receivePacket.getAddress(), receivePacket.getPort(),
						message.getId(), name);
				synchronized (nodes) {
					nodes.add(client);
					System.out.println("User added: "+client.getName());
				}
				SEntity entity = new SEntity();
            	entity.setController(new SDistantHumanControl(entity));
            	entity.setId(client.getId());
				client.getPlayer().setEntity(entity);
				SMain.getGameInstance().addEntity(entity);
				SMessage connectallowed = new SMessage(client.getId(),"CNNAP","");
				udpNode.SendMessage(connectallowed, client);
				RequestPingDataFromClient(client);
			}
		}else{
			System.out.println("User already joined: "+client.getName());
		}
	}
	
	private void ParseDisconnectCommand(SMessage message){
		SNode client = getNodeById(message.getId());
		if(client==null){
			System.out.println("User who wants to disconnect was not found: "+message.getId());
		}else{
			// TODO Look here if there is a client nullpointer problem
			synchronized (nodes) {
				nodes.remove(client);
				System.out.println("size of clients: "+nodes.size());
			}
			//TODO remove this insecure delete
			SMain.getGameInstance().removeEntity(client.getId());
			SMessage deleteentity = new SMessage(client.getId(),"DELEN","");
			SendMessage(deleteentity);
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
		String ping = SMessageParser.getPingCommandPrevPing(message);
		localNode.setPing(Float.parseFloat(ping));
		//Send back the same message to the server
		message.setCommandName("PNGAN");
		udpNode.SendMessage(message, server);
	}
	
	private void ParsePingAnswer(SMessage message){
		SNode client = getNodeById(message.getId());
		if (client!=null){
			String time = SMessageParser.getPingCommandTime(message);
			long nanoTime = Long.parseUnsignedLong(time);
			long ping = SServerTimer.GetNanoTime()-nanoTime;
			client.setPing((SServerTimer.GetNanoTime()-nanoTime)/1000/1000);
			System.out.println("ping: "+ping/1000/1000);
		}
	}
	private SNode getNodeById(UUID Id){
		synchronized (nodes) {
			for(SNode node : nodes){
				if(node.getId().equals(Id)){
					return node;
				}
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
	public List<SNode> getClients(){
		synchronized (nodes){
			return nodes;
		}
	}
	
}
