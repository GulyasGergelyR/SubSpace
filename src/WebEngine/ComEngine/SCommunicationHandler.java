package WebEngine.ComEngine;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import GameEngine.Specifications;
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
	private UDPNodeRole nodeRole;
	
	private LinkedList<SMessage> ObjectMessages;
	private LinkedList<SMessage> EntityMessages;
	
	public SCommunicationHandler(){
		nodes = new ArrayList<SNode>();
		ObjectMessages = new LinkedList<SMessage>();
		EntityMessages = new LinkedList<SMessage>();
	}
	public enum UDPNodeRole{
		Server, Client 
	}
	
	public SNode getLocalNode(){
		return localNode;
	}
	public void setLocalNode(SNode localNode){
		this.localNode = localNode;
	}
	
	public void createUDPNodeAsClient(int receivePort, int transmitPort){
		nodeRole = UDPNodeRole.Client;
		createUDPNode(receivePort, transmitPort);
	}
	public void createUDPNodeAsServer(int receivePort, int transmitPort){
		nodeRole = UDPNodeRole.Server;
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
		nodeRole = UDPNodeRole.Client;
		this.server = server;
		SMessage message = new SMessage(localNode.getId(), "CNNCL", localNode.getName());
		udpNode.SendMessage(message, server);
	}
	public void DisconnectFromServer(){
		localNode.setState(NodeState.NotConnected);
		SMessage message = new SMessage(localNode.getId(), "DSCCL", localNode.getName());
		udpNode.SendMessage(message, server);
	}
	public void SendMessage(SMessage message){
		if(nodeRole.equals(UDPNodeRole.Client)){
			if (localNode.getState().equals(NodeState.Connected))
				udpNode.SendMessage(message, server);
		}
		else if(nodeRole.equals(UDPNodeRole.Server)){
			for(SNode node : nodes){
				udpNode.SendMessage(message, node);
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
		if(message.isValid()){
			//Check what type of message is it
			String command = message.getCommandName();
			
			if (command.equals("CNNCL")){ //connect client
				ParseConnectCommand(receivePacket, message);
			}
			else if (command.equals("DSCCL")){ //disconnect client
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
			client = new SNode(receivePacket.getAddress(), receivePacket.getPort(),
					message.getId(), name);
			nodes.add(client);
		}else{
			System.out.println("User already joined: "+client.getName());
		}
	}
	
	private void ParseDisconnectCommand(DatagramPacket receivePacket, SMessage message){
		SNode client = getNodeById(message.getId());
		if(client==null){
			System.out.println("User who wants to disconnect was not found: "+message.getId());
		}else{
			nodes.remove(client);
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
		SNode client = getNodeById(message.getId());
		if (client!=null){
			int length = Integer.parseInt(message.content.substring(0, 2));
			String nanoTimeS = message.content.substring(3,3+length);
			long nanoTime = Long.parseUnsignedLong(nanoTimeS);
			client.setPing(SServerTimer.GetNanoTime()-nanoTime);
		}
	}
	private SNode getNodeById(UUID Id){
		for(SNode node : nodes){
			if(node.getId().equals(Id)){
				return node;
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
		return nodes;
	}
	
}
