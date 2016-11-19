package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SObject.ObjectState;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SSimpleBulletControlServer extends SControlServer {
	protected int maxLifeTime = 100;
	protected int currentLifeTime = 0;
	protected int maxLifeDistance = 100;
	
	public SSimpleBulletControlServer(SMobile mobile){
		super(mobile);
	}
	@Override
	protected void Think() {
		if (currentLifeTime < maxLifeTime){
			currentLifeTime++;
		}
		else{
			// Delete this
			Owner.setObjectState(ObjectState.WaitingDelete);
			SM message = SMPatterns.getObjectDeleteMessage(Owner);
			SMain.getCommunicationHandler().SendMessage(message);
		}
	}
}
