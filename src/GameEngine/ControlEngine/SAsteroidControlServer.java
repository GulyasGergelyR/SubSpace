package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SObject.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SGeomFunctions;
import GameEngine.ObjectEngine.DebrisEngine.SDebrisFactory;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SAsteroidControlServer extends SControlServer{
	int sendCounter = 0;
	int maxSendCounter = 60;
	public SAsteroidControlServer(SMobile mobile){
		super(mobile);
	}
	@Override
	protected void Think() {
		for(SEntity entity : SMain.getGameInstance().getEntities()){
			if (SGeomFunctions.intersects(entity, Owner)){
				entity.gotHit(1000);
				// add explosion to client
				SM explosionMessage = SMPatterns.getAnimationObjectCreateMessage(Owner.getPos(), (byte)61);
				SMain.getCommunicationHandler().SendMessage(explosionMessage);
			}
		}
		/*
		for(SObject object : SDebrisFactory.getObjects()){
			if (SGeomFunctions.intersects(object, Owner)){
				Owner.setObjectState(ObjectState.WaitingDelete);
				SM message = SMPatterns.getObjectDeleteMessage(Owner);
				SMain.getCommunicationHandler().SendMessage(message);
				// add explosion to client
				SM explosionMessage = SMPatterns.getAnimationObjectCreateMessage(Owner.getPos(), (byte)61);
				SMain.getCommunicationHandler().SendMessage(explosionMessage);
				break;
			}
		}*/
		if (sendCounter > maxSendCounter){
			sendCounter = 0;
			SM message = SMPatterns.getObjectUpdateMessage(Owner);
			SMain.getCommunicationHandler().SendMessage(message);
		}else{
			sendCounter++;
		}
		
		if (Owner.getObjectState().equals(ObjectState.Active)){
			if ((Math.abs(Owner.getPos().getX()) > 5500) || 
					(Math.abs(Owner.getPos().getY()) > 5500)){
				// Delete this
				Owner.setObjectState(ObjectState.WaitingDelete);
				SM message = SMPatterns.getObjectDeleteMessage(Owner);
				SMain.getCommunicationHandler().SendMessage(message);
				SDebrisFactory.deletedDebris(SDebrisFactory.Asteroid);
				}
			}
		}
}
