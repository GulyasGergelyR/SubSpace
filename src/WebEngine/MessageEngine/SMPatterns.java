package WebEngine.MessageEngine;

import java.nio.ByteBuffer;

import GameEngine.SPlayer;
import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SObject;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.WeaponEngine.SBullet;
import WebEngine.ComEngine.SNode;

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
	public static byte CObjectDelete = 0x17;
	
	public static SM getConnectToServerMessage(String nameString){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CConnect);
		byte[] name = nameString.getBytes();
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
	public static SM getEntityUpdateMessage(SEntity entity){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CEntityUpdate);
		buffer.putShort((short)entity.getId().get());
		// Add vectors
		entity.getPos().addToBufferAsBigVector(buffer);
		entity.getLookDir().addToBufferAsBigVector(buffer);
		entity.getMoveDir().addToBufferAsBigVector(buffer);
		entity.getAcclDir().addToBufferAsBigVector(buffer);
		return message;
	}
	public static SM getEntityCreateMessage(SPlayer player){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CEntityCreate);
		buffer.putShort((short)player.getId().get());
		byte[] name = player.getName().getBytes();
		buffer.put((byte)player.getName().length());
		buffer.put(name);
		return message;
	}
	public static SM getEntityDeleteMessage(SEntity entity){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CEntityDelete);
		buffer.putShort((short)entity.getId().get());
		return message;
	}
	
	public static SM getClientUpdateMessage(SMobile mobile, byte command, SVector aim){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CClientInput);
		buffer.putShort((short)mobile.getId().get());
		buffer.put(command);
		aim.addToBufferAsBigVector(buffer);
		return message;
	}
	
	public static SM getObjectUpdateMessage(SObject object){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CObjectUpdate);
		buffer.putShort((short)object.getId().get());
		// Add vectors
		object.getPos().addToBufferAsBigVector(buffer);
		object.getLookDir().addToBufferAsBigVector(buffer);
		return message;
	}
	public static SM getObjectCreateMessage(SObject object){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CObjectCreate);
		buffer.putShort((short)object.getId().get());
		if (object instanceof SBullet){
			buffer.put((byte)20); //TODO remove hard coded bullet type id
			buffer.putShort((short)(((SBullet)object).getOwner().getId().get()));
		}
		return message;
	}
	public static SM getObjectDeleteMessage(SObject object){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CObjectDelete);
		buffer.putShort((short)object.getId().get());
		return message;
	}
	
}
