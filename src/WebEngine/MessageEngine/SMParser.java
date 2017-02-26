package WebEngine.MessageEngine;

import java.nio.ByteBuffer;

import GameEngine.SGameInstance;
import GameEngine.SId;
import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SUpdatable;
import GameEngine.ControlEngine.SControl;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SBulletExplosion;
import GameEngine.ObjectEngine.SExplosion;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.DebrisEngine.SDebris;
import GameEngine.ObjectEngine.EffectEngine.SEffect;
import GameEngine.WeaponEngine.SBullet;
import Main.SMain;

public class SMParser {
	public static int parseId(ByteBuffer buffer){ // 2 byte long
		return buffer.getShort();
	}
	public static SVector parseBigVector(ByteBuffer buffer){ //[+-32768],[9999] - 4 byte long
		float x =  buffer.getShort()+ buffer.getShort()/30000f;
		float y =  buffer.getShort()+ buffer.getShort()/30000f;
		return new SVector(x,y);
	}
	public static SVector parseSmallVector(ByteBuffer buffer){ //[+-127],[9999] - 3 byte long
		float x =  buffer.get()+ buffer.getShort()/30000f;
		float y =  buffer.get()+ buffer.getShort()/30000f;
		return new SVector(x,y);
	}
	public static SVector parseNormedVector(ByteBuffer buffer){ //[+-127],[9999] - 3 byte long
		float x =  buffer.getShort()/30000f;
		float y =  buffer.getShort()/30000f;
		return new SVector(x,y).norm();
	}
	
	public static void parseEntityUpdateMessage(SM message, SEntity entity){
		ByteBuffer buffer = message.getBuffer();
		entity.setPos(parseBigVector(buffer));
		float angle = buffer.getShort()/100f;
		entity.setLookDir(new SVector(1,0).rotate(-angle));
		//entity.setMoveDir(parseBigVector(buffer));
		//entity.setAcclDir(parseBigVector(buffer));
		
		//TODO check if this is needed - posUpdated
		entity.setPosUpdated();
	}
	public static void parseEntityUpdateStateMessage(SM message, SEntity entity){
		ByteBuffer buffer = message.getBuffer();
		entity.setObjectState(buffer.get());
		entity.setPlayerGameState(buffer.get());
		entity.setLife(buffer.getShort());
		entity.setShield(buffer.getShort());
		entity.getPlayer().setKills(buffer.get());
		entity.getPlayer().setDeaths(buffer.get());
	}
	public static void parseEntityCreateMessageAtServer(SM message){
		// Copy of the ConnectAllowed message
		// Server side communication handler sent this. not a client!
		// The server cannot receive a message like this, however when the message sent to the client
		// the server will pass it to the server side gameInstance as well
		ByteBuffer buffer = message.getBuffer();
		int id = SMParser.parseId(buffer);
		SFH.Entities.createEntityAtServer(id);
	}
	public static void parseEntityCreateMessageAtClient(SM message){
		ByteBuffer buffer = message.getBuffer();
		int id = SMParser.parseId(buffer);
		byte nameLength = buffer.get();
		if (nameLength<0){
			System.out.println("CLIENT NAME ERROR");
			return;
		}
		byte[] nameBytes = new byte[nameLength];
		for (int i=0; i<nameLength;i++){
			nameBytes[i] = buffer.get();
		}
		String name = new String(nameBytes);
		SFH.Entities.createEntityAtClient(id, name);
	}
	public static void parseClientInputMessage(SM message, SEntity entity){
		SControl control = entity.getController();
		ByteBuffer buffer = message.getBuffer();
		byte command = buffer.get();
		SVector aimLookDir = parseBigVector(buffer);
		entity.setAimLookDir(aimLookDir);
		for (byte key=0;key<4;key++){
			boolean pressed = false;
			int state = (byte)(command >> key) & 1;
			if (state == 1){
				pressed = true;
			}
			control.setKeyTo(key, pressed);
		}
		for (byte key=0;key<2;key++){
			boolean pressed = false;
			int state = (byte)(command >> (key+4)) & 1;
			if (state == 1){
				pressed = true;
			}
			control.setMouseTo(key, pressed);
		}
	}
	public static void parseObjectCreateMessage(SM message){
		ByteBuffer buffer = message.getBuffer();
		int id = SMParser.parseId(buffer);
		SObject object = null;
		int objectTypeId = buffer.get();
		if (objectTypeId == 20){  // TODO remove hard coded bullet type id
			int ownerId = buffer.getShort();
			SVector pos = parseBigVector(buffer);
			SVector movedir = parseBigVector(buffer);
			SVector lookdir = new SVector(movedir);
			object = new SBullet(ownerId, pos, lookdir, movedir);
			object.setId(new SId(id));
			SMain.getGameInstance().addObject(object);
		} else if (objectTypeId == SFH.PowerUps.getFactoryType()){
			byte powerUpType = buffer.get();
			SVector pos = parseBigVector(buffer);
			SFH.PowerUps.createNewPowerUpAtClient(pos, id, powerUpType);
		} else if (objectTypeId == SFH.Debris.getFactoryType()){
			byte debriesType = buffer.get();
			SVector pos = parseBigVector(buffer);
			SVector moveDir = parseBigVector(buffer);
			float scale = buffer.getShort() / 1000.0f;
			SFH.Debris.createNewDebrisAtClient(pos, moveDir, scale, id, debriesType);
		} else if (objectTypeId == SFH.Effects.getFactoryType()){
			byte effectType = buffer.get();
			int ownerId = buffer.getShort();
			SFH.Effects.createNewEffectAtClient(id, ownerId, effectType);
		}
	}
	public static void parseAnimationObjectCreateMessage(SM message){
		ByteBuffer buffer = message.getBuffer();
		SObject object = null;
		byte animationId = buffer.get();
		SVector pos = parseBigVector(buffer);
		if (animationId == 60){
			//explosion
			object = new SExplosion(pos);
			SMain.getGameInstance().addAnimationObject(object);
		}
		else if(animationId == 61){  //bullet explosion
			object = new SBulletExplosion(pos);
			SMain.getGameInstance().addAnimationObject(object);
		}
	}
	public static void parseObjectRequestCreateMessage(SM message){
		ByteBuffer buffer = message.getBuffer();
		int id = SMParser.parseId(buffer);
		byte factoryId = buffer.get();
		SUpdatable updatable = null;
		if (factoryId == 20){  // TODO remove hard coded bullet type id
			updatable = SMain.getGameInstance().getObjectById(id);
		} else if (factoryId == SFH.PowerUps.getFactoryType()){
			updatable = SFH.PowerUps.getObjectById(id);
		} else if (factoryId == SFH.Debris.getFactoryType()){
			updatable = SFH.Debris.getObjectById(id);
		} else if (factoryId == SFH.Effects.getFactoryType()){
			updatable = SFH.Effects.getObjectById(id);
		}
		if (updatable != null && updatable.isActive()){
			SM messageCreate = SMPatterns.getObjectCreateMessage(updatable);
        	SMain.getCommunicationHandler().SendMessageToNode(messageCreate, SMain.getCommunicationHandler().getNodeByAddress(message.getAddress()));
		}
	}
	public static void parseObjectUpdateMessage(SM message){
		ByteBuffer buffer = message.getBuffer();
		int id = SMParser.parseId(buffer);
		int objectTypeId = buffer.get();
		if (objectTypeId == SFH.Debris.getFactoryType()){
			SDebris debris = SFH.Debris.getObjectByIdWithCheck(id);
			if (debris == null){
				return;
			}
			debris.setPos(parseBigVector(buffer));
			debris.setMoveDir(parseBigVector(buffer));
		} else if (objectTypeId == SFH.Effects.getFactoryType()){
			SEffect effect = SFH.Effects.getObjectByIdWithCheck(id);
			if (effect == null){
				return;
			}
			effect.setCurrentTime(buffer.getShort());
		}
	}
	public static void parseObjectDeleteMessage(SM message, SGameInstance gameInstance){
		ByteBuffer buffer = message.getBuffer();
		int id = SMParser.parseId(buffer);
		byte type = buffer.get();
		//TODO remove gameInstance
		if (type == 0){
			gameInstance.removeObjectFromList(id);
		} else {
			SFH.removeObjectFromList(type, id);
		}
	}
}
