package WebEngine.MessageEngine;

import java.nio.ByteBuffer;

import GameEngine.SId;
import GameEngine.SPlayer;
import GameEngine.ObjectEngine.SExplosion;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUpFactory;
import GameEngine.SPlayer.PlayerState;
import GameEngine.BaseEngine.SObject;
import GameEngine.ControlEngine.SControl;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.WeaponEngine.SBullet;
import Main.SMain;
import WebEngine.ComEngine.SNode;

public class SMParser {
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
	
	
	public static void parseEntityUpdateMessage(SM message, SEntity entity){
		ByteBuffer buffer = message.getBuffer();
		entity.setPos(parseBigVector(buffer));
		entity.setLookDir(parseBigVector(buffer));
		entity.setMoveDir(parseBigVector(buffer));
		entity.setAcclDir(parseBigVector(buffer));
		entity.setLife(buffer.getShort());
		entity.getPlayer().setKills(buffer.get());
		entity.getPlayer().setDeaths(buffer.get());
		//TODO check if this is needed - posUpdated
		entity.setPosUpdated();
	}
	public static void parseEntityUpdateStateMessage(SM message, SEntity entity){
		ByteBuffer buffer = message.getBuffer();
		entity.setObjectState(buffer.get());
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
			 SPlayer player = new SPlayer(id, name, PlayerState.lan);
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
			SVector lookdir = parseBigVector(buffer);
			SVector movedir = parseBigVector(buffer);
			object = new SBullet(ownerId, pos, lookdir, movedir);
			object.setId(new SId(id));
			SMain.getGameInstance().addObject(object);
		} else if (objectTypeId == 40){  // TODO remove hard coded power up type id
			byte powerUpType = buffer.get();
			SVector pos = parseBigVector(buffer);
			SPowerUpFactory.createNewPowerUpAtClient(pos, id, powerUpType);
		}
	}
	public static void parseAnimationObjectCreateMessage(SM message){
		ByteBuffer buffer = message.getBuffer();
		SObject object = null;
		SVector pos = parseBigVector(buffer);
		object = new SExplosion(pos);
		SMain.getGameInstance().addAnimationObject(object);
	}
	public static void parseObjectUpdateMessage(SM message, SObject object){
		ByteBuffer buffer = message.getBuffer();
		object.setPos(parseBigVector(buffer));
		object.setLookDir(parseBigVector(buffer));
	}
}
