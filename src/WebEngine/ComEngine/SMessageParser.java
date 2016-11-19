package WebEngine.ComEngine;

import java.util.LinkedList;

import GameEngine.EntityEngine.SControl;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;


public class SMessageParser {
	private SCommunicationHandler cHandler;
	
	public SMessageParser(SCommunicationHandler communicationHandler){
		this.cHandler = communicationHandler;
	}
	
	public static boolean ParseEntityUpdateMessage(SMessage message, SEntity entity){
		entity.setPos(SMessagePatterns.getPos(message));
		entity.setLookDir(SMessagePatterns.getLookDir(message));
		entity.setMoveDir(SMessagePatterns.getMoveDir(message));
		entity.setAcclDir(SMessagePatterns.getAcclDir(message));
		entity.setPosUpdated();
		
		return true;
	}
	public static boolean ParseClientInputMessage(SMessage message, SEntity entity){
		SControl control = entity.getController();
		LinkedList<String> wasd = SMessagePatterns.getEntityCommandWASD(message);
		SVector aimLookDir = SMessagePatterns.getMousePos(message);
		if (aimLookDir!= null){
			entity.setAimLookDir(aimLookDir);
		}
		while(wasd.size()>0){
			String command = wasd.pop();
			boolean pressed = false;
			if(command.substring(0, 1).equals("P")){
				pressed = true;
			} //"R" - Released is default false
			control.setKeyTo(command.substring(1, 2), pressed);
		}
		return true;
	}
}