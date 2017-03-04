package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SEntity.PlayerGameState;
import GameEngine.GeomEngine.SCollision;
import GameEngine.GeomEngine.SGeomFunctions;
import GameEngine.GeomEngine.SInteraction;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.DebrisEngine.SDebris;
import GameEngine.WeaponEngine.SBullet;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SSimpleBulletControlServer extends SControlServer<SBullet> {
	protected int maxLifeTime = 200;
	protected int currentLifeTime = 0;
	protected int maxLifeDistance = 100;
	
	public SSimpleBulletControlServer(SBullet mobile){
		super(mobile);
	}
	@Override
	protected void Think() {
		for(SEntity entity : SFH.Entities.getObjects()){
			SEntity bulletOwner = Owner.getOwner();
			if (!entity.equals(bulletOwner) && entity.getObjectState().equals(ObjectState.Active) &&
					entity.getPlayerGameState().equals(PlayerGameState.Alive)){
				SInteraction interaction = new SInteraction(entity, Owner);
				if (interaction.IsHappened()){
					Owner.setObjectState(ObjectState.WaitingDelete);
					SM message = SMPatterns.getObjectDeleteMessage(Owner);
					SMain.getCommunicationHandler().SendMessage(message);
					break;
				}
			}
		}
		for(SDebris object : SFH.Debris.getObjects()){
			if (object.getObjectState().equals(ObjectState.Active)){
				SInteraction interaction = new SInteraction(object, Owner);
				if (interaction.IsHappened()){
					((SDebris)object).getController().setSendCounter(0);
					SM message = SMPatterns.getObjectUpdateMessage(object);
					SMain.getCommunicationHandler().SendMessage(message);
					
					Owner.setObjectState(ObjectState.WaitingDelete);
					SM messageObjectDelete = SMPatterns.getObjectDeleteMessage(Owner);
					SMain.getCommunicationHandler().SendMessage(messageObjectDelete);
					break;
				}
			}
		}
		if (Owner.getObjectState().equals(ObjectState.Active)){
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
	@Override
	public void ThinkAndAct() {
		Think();
		if (Owner.getObjectState().equals(ObjectState.Active)){
			Act();
		}
	}
	
	
}
