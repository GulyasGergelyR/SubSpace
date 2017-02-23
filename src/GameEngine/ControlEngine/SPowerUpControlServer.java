package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SObject.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SEntity.PlayerGameState;
import GameEngine.GeomEngine.SGeomFunctions;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUp;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUpFactory;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SPowerUpControlServer extends SControlServer{
	
	// For being activeS
	protected int duration = 0;  // it means its there forever
	protected int currentTime = 0;
		
	public SPowerUpControlServer(SMobile mobile){
		super(mobile);
	}
	
	@Override
	protected void Think() {
		if (duration > 0){
			currentTime++;
			if (currentTime >= duration){
				Owner.setObjectState(ObjectState.WaitingDelete);
				SM message = SMPatterns.getObjectDeleteMessage(Owner);
				SMain.getCommunicationHandler().SendMessage(message);
				return;
			}
		}
		
		for(SEntity entity : SMain.getGameInstance().getEntities()){
			if(	entity.getPlayerGameState().equals(PlayerGameState.Alive) &&
					entity.getObjectState().equals(ObjectState.Active)){
				if (SGeomFunctions.intersects(entity, Owner)){
					if (!((SPowerUp)Owner).applyToEntity(entity))
						continue;
					Owner.setObjectState(ObjectState.WaitingDelete);
					SM message = SMPatterns.getObjectDeleteMessage(Owner);
					SMain.getCommunicationHandler().SendMessage(message);
					SPowerUpFactory.powerUpApplied(((SPowerUp)Owner).getType());
					break;
				}
			}
		}
	}
	@Override
	public void ThinkAndAct() {
		Think();
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
