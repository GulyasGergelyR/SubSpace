package WebEngine.MessageEngine;

import java.nio.ByteBuffer;

import GameEngine.SPlayer;
import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SUpdatable;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.DebrisEngine.SAsteroid;
import GameEngine.ObjectEngine.DebrisEngine.SDebris;
import GameEngine.ObjectEngine.EffectEngine.SEffect;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUp;
import GameEngine.WeaponEngine.SBullet;
import WebEngine.ComEngine.SNode;

public class SMPatterns {
	//TODO create ID adder function instead of adding simple short, for future changes
	
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
	public static byte CEntityUpdateState = 0x1B;
	public static byte CEntityDelete = 0x1F;
	
	//0001 0xxx Object commands
	public static byte MObject = 0x10;
	public static byte CObjectCreate = 0x11;
	public static byte CObjectUpdate = 0x12;
	public static byte CObjectDelete = 0x17;
	
	//TODO sort this out
	public static byte CAnimationObjectCreate = 0x13;
	
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
		buffer.putShort((short)entity.getLife());
		buffer.putShort((short)entity.getShield());
		buffer.put((byte)entity.getPlayer().getKills());
		buffer.put((byte)entity.getPlayer().getDeaths());
		return message;
	}
	public static SM getEntityUpdateStateMessage(SEntity entity){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CEntityUpdateState);
		buffer.putShort((short)entity.getId().get());
		buffer.put(entity.getObjectStateId());
		buffer.put(entity.getPlayerGameStateId());
		
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
	
	public static SM getObjectUpdateMessage(SUpdatable object){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CObjectUpdate);
		buffer.putShort((short)object.getId().get());
		// Add vectors
		if (object instanceof SAsteroid){
			SAsteroid asteroid = (SAsteroid) object;
			buffer.put((byte)50);
			asteroid.getPos().addToBufferAsBigVector(buffer);
			asteroid.getMoveDir().addToBufferAsBigVector(buffer);
		} else if (object instanceof SEffect){
			buffer.put((byte)70);
			buffer.putShort((short)0);	//reset currentTime by default
		}
		return message;
	}
	public static SM getObjectCreateMessage(SUpdatable object){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CObjectCreate);
		buffer.putShort((short)object.getId().get());
		if (object instanceof SBullet){
			SBullet bullet = (SBullet) object;
			buffer.put((byte)20); //TODO remove hard coded bullet type id
			buffer.putShort((short)(bullet.getOwner().getId().get()));
			bullet.getPos().addToBufferAsBigVector(buffer);
			bullet.getLookDir().addToBufferAsBigVector(buffer);
			bullet.getMoveDir().addToBufferAsBigVector(buffer);
		} else if (object instanceof SPowerUp){
			SPowerUp powerUp = (SPowerUp) object;
			buffer.put((byte)40); //TODO remove hard coded power up type id
			buffer.put(powerUp.getType());
			powerUp.getPos().addToBufferAsBigVector(buffer);
		} else if (object instanceof SDebris){
			SDebris debris = (SDebris) object;
			buffer.put((byte)50); //TODO remove hard coded debris type id
			buffer.put(debris.getType());
			debris.getPos().addToBufferAsBigVector(buffer);
			debris.getMoveDir().addToBufferAsBigVector(buffer);
			buffer.putShort((short)(debris.getBody().getScale()*1000));
		} else if (object instanceof SEffect){
			SEffect effect = (SEffect) object;
			buffer.put((byte)70); //TODO remove hard coded effect type id
			buffer.put(effect.getType());
			buffer.putShort((short)(effect.getOwner().getId().get()));
		}
		return message;
	}
	
	public static SM getAnimationObjectCreateMessage(SVector pos, byte animationId){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CAnimationObjectCreate);
		buffer.put(animationId);
		pos.addToBufferAsBigVector(buffer);
		return message;
	}
	
	public static SM getObjectDeleteMessage(SUpdatable object){
		SM message = new SM();
		ByteBuffer buffer = message.getBuffer();
		buffer.put(CObjectDelete);
		buffer.putShort((short)object.getId().get());
		if (object instanceof SAsteroid){
			buffer.put((byte)50);
		} else if (object instanceof SPowerUp){
			buffer.put((byte)40);
		} else if (object instanceof SEffect){
			buffer.put((byte)70);
		}
		return message;
	}
	
}
