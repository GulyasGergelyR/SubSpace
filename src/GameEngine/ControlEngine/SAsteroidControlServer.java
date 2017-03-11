package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SEntity.PlayerGameState;
import GameEngine.GeomEngine.SCollision;
import GameEngine.GeomEngine.SGeomFunctions;
import GameEngine.GeomEngine.SInteraction;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.DebrisEngine.SAsteroid;
import GameEngine.ObjectEngine.DebrisEngine.SDebris;
import GameEngine.ObjectEngine.DebrisEngine.SDebrisFactory;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SAsteroidControlServer extends SControlServer<SDebris>{
	public SAsteroidControlServer(SDebris mobile){
		super(mobile);
	}
	@Override
	protected void Think() {
		for(SEntity entity : SFH.Entities.getObjects()){
			if (entity.getObjectState().equals(ObjectState.Active) &&
					entity.getPlayerGameState().equals(PlayerGameState.Alive)){
				SInteraction interaction = new SInteraction(entity, Owner);
				if (interaction.IsHappened()){
						Owner.getController().setSendCounter(0);
						SM message = SMPatterns.getObjectUpdateMessage((SAsteroid)Owner);
						SMain.getCommunicationHandler().SendMessage(message);
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
