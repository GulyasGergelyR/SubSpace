package WebEngine.MessageEngine;

import java.nio.ByteBuffer;

import WebEngine.ComEngine.SNode;
import GameEngine.GeomEngine.SVector;

public class SMPatterns {
	//Message Patterns
	//M is mask, C is command
	
	//0111 xxxx Connect commands
	public static byte MConnect = 0x70;
	public static byte CConnect = 0x71;
	public static byte CDisconnect = 0x7F;
	public static byte CConnectAllowed = 0x73;
	public static byte CConnectNotAllowed = 0x74;
	
	//0100 xxxx Ping commands
	public static byte MPing = 0x40;
	public static byte CPingRequest = 0x41;
	public static byte CPingAnswer = 0x42;
	
	//0011 xxxx Client input commands
	public static byte MClientInput = 0x30;
	public static byte CClientInput = 0x31;
	
	//0001 xxxx Game commands
	public static byte MGameComamnd = 0x10;
	
	//0001 1xxx Entity commands
	public static byte MEntity = 0x14;
	public static byte CEntityCreate = 0x19;
	public static byte CEntityUpdate = 0x1A;
	public static byte CEntityDelete = 0x1F;
	
	//0001 0xxx Object commands
	public static byte MObject = 0x10;
	public static byte CObjectCreate = 0x11;
	public static byte CObjectUpdate = 0x12;
	public static byte CObjectDelete = 0x1F;
	
	public static int parseId(ByteBuffer buffer){ // 2 byte long
		return buffer.getShort();
	}
	public static SVector parseBigVector(ByteBuffer buffer){ //[+-32768],[9999] - 4 byte long
		float x =  buffer.getShort()+ buffer.getShort()/10000f;
		float y =  buffer.getShort()+ buffer.getShort()/10000f;
		return new SVector(x,y);
	}
	public static SVector parseSmallVector(ByteBuffer buffer){ //[+-127],[9999] - 3 byte long
		float x =  buffer.get()+ buffer.getShort()/10000f;
		float y =  buffer.get()+ buffer.getShort()/10000f;
		return new SVector(x,y);
	}
	
	private static byte[] getIdBytes(int id){
		return ByteBuffer.allocate(2).putShort((short)id).array();
	}
	private static byte[] getShortBytes(short value){
		return ByteBuffer.allocate(2).putShort(value).array();
	}
	private static byte[] getLongBytes(long value){
		return ByteBuffer.allocate(8).putLong(value).array();
	}
	
	public static SM getConnectToServerMessage(String nameString){
		byte[] name = nameString.getBytes();
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CConnect);
		buffer.put((byte)nameString.length());
		buffer.put(name);
		return message;
	}
	public static SM getDisconnectFromServerMessage(SNode localNode){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CDisconnect);
		buffer.putShort((short)localNode.getId().get());
		return message;
	}
	public static SM getPingRequestMessage(SNode client, long nanoTime){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CPingRequest);
		buffer.putLong(nanoTime);
		if (client.getPing()>999){
			buffer.putShort((short)999);
		}else{
			buffer.putShort((short)client.getPing());
		}
		return message;
	}
	public static SM getPingAnswerMessage(long nanoTime){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CPingAnswer);
		buffer.putLong(nanoTime);
		return message;
	}
	public static SM getConnectAllowedMessage(SNode client){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CConnectAllowed);
		buffer.putShort((short)client.getId().get());
		return message;
	}
}
