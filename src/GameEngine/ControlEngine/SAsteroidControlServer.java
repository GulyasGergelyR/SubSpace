package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SEntity.PlayerGameState;
import GameEngine.GeomEngine.SGeomFunctions;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.DebrisEngine.SAsteroid;
import GameEngine.ObjectEngine.DebrisEngine.SDebrisFactory;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SAsteroidControlServer extends SControlServer{
	public SAsteroidControlServer(SMobile mobile){
		super(mobile);
	}
	@Override
	protected void Think() {
		for(SEntity entity : SFH.Entities.getObjects()){
			if (entity.getObjectState().equals(ObjectState.Active) &&
					entity.getPlayerGameState().equals(PlayerGameState.Alive)){
				if (SGeomFunctions.intersects(entity, Owner)){
					if (SGeomFunctions.collide((SAsteroid)Owner, entity)){
						if (!entity.gotHit(20, Owner)){
							SM explosionMessage = SMPatterns.getAnimationObjectCreateMessage(entity.getPos(), (byte)61);
							SMain.getCommunicationHandler().SendMessage(explosionMessage);
						}
						
						Owner.getController().setSendCounter(0);
						SM message = SMPatterns.getObjectUpdateMessage((SAsteroid)Owner);
						SMain.getCommunicationHandler().SendMessage(message);
					}
				}
			}
		}
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
				SFH.Debris.deletedDebris(SDebrisFactory.Asteroid);
				}
			}
		}
}
