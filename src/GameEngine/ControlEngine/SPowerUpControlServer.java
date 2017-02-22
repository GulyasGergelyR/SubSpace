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
	
	
	public SPowerUpControlServer(SMobile mobile){
		super(mobile);
	}
	
	@Override
	protected void Think() {
		for(SEntity entity : SMain.getGameInstance().getEntities()){
			if (SGeomFunctions.intersects(entity, Owner) &&
					entity.getPlayerGameState().equals(PlayerGameState.Alive) &&
					entity.getObjectState().equals(ObjectState.Active)){
				if (!((SPowerUp)Owner).applyToEntity(entity))
					continue;
				Owner.setObjectState(ObjectState.WaitingDelete);
				SM message = SMPatterns.getObjectDeleteMessage(Owner);
				SMain.getCommunicationHandler().SendMessage(message);
				SPowerUpFactory.powerUpApplied();
				break;
			}
		}
	}
	@Override
	public void ThinkAndAct() {
		Think();
	}
}
