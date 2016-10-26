package WebEngine.ComEngine;

import GameEngine.EntityEngine.SEntity;


public class SMessageParser {
	private SCommunicationHandler cHandler;
	
	public SMessageParser(SCommunicationHandler communicationHandler){
		this.cHandler = communicationHandler;
	}
	
	public static boolean ParseEntityMessage(SMessage message, SEntity entity){
		entity.setPos(SMessagePatterns.getPos(message));
		entity.setLookDir(SMessagePatterns.getLookDir(message));
		entity.setMoveDir(SMessagePatterns.getMoveDir(message));
		entity.setAcclDir(SMessagePatterns.getAcclDir(message));
		entity.setPosUpdated();
		return true;
	}
}