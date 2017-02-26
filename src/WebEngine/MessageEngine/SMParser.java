package WebEngine.MessageEngine;

import java.nio.ByteBuffer;

import GameEngine.SGameInstance;
import GameEngine.SId;
import GameEngine.SPlayer;
import GameEngine.SPlayer.PlayerType;
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
import WebEngine.ComEngine.SNode;

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
		entity.setLookDir(parseBigVector(buffer));
		entity.setMoveDir(parseBigVector(buffer));
		entity.setAcclDir(parseBigVector(buffer));
		entity.setLife(buffer.getShort());
		entity.setShield(buffer.getShort());
		entity.getPlayer().setKills(buffer.get());
		entity.getPlayer().setDeaths(buffer.get());
		//TODO check if this is needed - posUpdated
		entity.setPosUpdated();
	}
	public static void parseEntityUpdateStateMessage(SM message, SEntity entity){
		ByteBuffer buffer = message.getBuffer();
		entity.setObjectState(buffer.get());
		entity.setPlayerGameState(buffer.get());
	}
	public static void parseEntityCreateMessage(SM message){
		ByteBuffer buffer = message.getBuffer();
		int id = SMParser.parseId(buffer);
		
		SNode localNode = SMain.getCommunicationHandler().getLocalNode();
		
		if (localNode.equals(id)){
			SEntity entity = new SEntity(localNode.getPlayer());
			SMain.getGameInstance().addEntity(entity);
		}
		else{
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
			 SPlayer player = new SPlayer(id, name, PlayerType.lan);
			 SEntity entity = new SEntity(player);
			 SMain.getGameInstance().addPlayer(player);
			 SMain.getGameInstance().addEntity(entity);
		}
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
		} else if (objectTypeId == 40){  // TODO remove hard coded power up type id
			byte powerUpType = buffer.get();
			SVector pos = parseBigVector(buffer);
			SFH.PowerUps.createNewPowerUpAtClient(pos, id, powerUpType);
		} else if (objectTypeId == 50){  // TODO remove hard coded debris type id
			byte debriesType = buffer.get();
			SVector pos = parseBigVector(buffer);
			SVector moveDir = parseBigVector(buffer);
			float scale = buffer.getShort() / 1000.0f;
			SFH.Debris.createNewDebrisAtClient(pos, moveDir, scale, id, debriesType);
		} else if (objectTypeId == 70){  // TODO remove hard coded effect type id
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
		} else if (factoryId == 40){  // TODO remove hard coded power up type id
			updatable = SFH.PowerUps.getObjectById(id, false);
		} else if (factoryId == 50){  // TODO remove hard coded debris type id
			updatable = SFH.Debris.getObjectById(id, false);
		} else if (factoryId == 70){  // TODO remove hard coded effect type id
			updatable = SFH.Effects.getObjectById(id, false);
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
		if (objectTypeId == 50){  // TODO remove hard coded debris type id
			SDebris debris = SFH.Debris.getObjectById(id, true);
			if (debris == null){
				return;
			}
			debris.setPos(parseBigVector(buffer));
			debris.setMoveDir(parseBigVector(buffer));
		} else if (objectTypeId == 70){  // TODO remove hard coded effect type id
			SEffect effect = SFH.Effects.getObjectById(id, true);
			if (effect == null){
				return;
			}
			effect.setCurrentTime(buffer.getShort());
		}
	}
	public static void parseObjectDeleteMessage(SM message, SGameInstance gameInstance){
		ByteBuffer buffer = message.getBuffer();
		int id = SMParser.parseId(buffer);
		int type = buffer.get();
		if (type == 0){
			gameInstance.removeObjectFromList(id);
		} else if (type == 40){
			SFH.PowerUps.removeObjectFromList(id);
		} else if (type == 50){
			SFH.Debris.removeObjectFromList(id);
		} else if (type == 70){
			SFH.Effects.removeObjectFromList(id);
		}
	}
}
