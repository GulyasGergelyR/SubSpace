package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SEntity.PlayerGameState;
import GameEngine.GeomEngine.SInteraction;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUp;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SPowerUpControlServer extends SControlServer<SPowerUp>{
	
	// For being activeS
	protected int duration = 0;  // it means its there forever
	protected int currentTime = 0;
		
	public SPowerUpControlServer(SPowerUp mobile){
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
				SFH.PowerUps.powerUpApplied(Owner.getType());
				return;
			}
		}
		
		for(SEntity entity : SFH.Entities.getObjects()){
			if(	entity.getPlayerGameState().equals(PlayerGameState.Alive) &&
					entity.getObjectState().equals(ObjectState.Active)){
				SInteraction interaction = new SInteraction(entity, Owner);
				if (interaction.IsHappened()){
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
