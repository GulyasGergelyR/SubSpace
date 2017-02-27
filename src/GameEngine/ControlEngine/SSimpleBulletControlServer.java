package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SEntity.PlayerGameState;
import GameEngine.GeomEngine.SGeomFunctions;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.DebrisEngine.SAsteroid;
import GameEngine.ObjectEngine.DebrisEngine.SDebris;
import GameEngine.WeaponEngine.SBullet;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SSimpleBulletControlServer extends SControlServer {
	protected int maxLifeTime = 200;
	protected int currentLifeTime = 0;
	protected int maxLifeDistance = 100;
	
	public SSimpleBulletControlServer(SMobile mobile){
		super(mobile);
	}
	@Override
	protected void Think() {
		for(SEntity entity : SFH.Entities.getObjects()){
			SEntity bulletOwner = ((SBullet)Owner).getOwner();
			if (!entity.equals(bulletOwner) && entity.getObjectState().equals(ObjectState.Active) &&
					entity.getPlayerGameState().equals(PlayerGameState.Alive)){
				if (SGeomFunctions.intersects(entity, Owner)){
					if (entity.gotHit(((SBullet)Owner).getDamage(), Owner))
						bulletOwner.getPlayer().addKill(1);
					Owner.setObjectState(ObjectState.WaitingDelete);
					SM message = SMPatterns.getObjectDeleteMessage(Owner);
					SMain.getCommunicationHandler().SendMessage(message);
					// add explosion to client
					SM explosionMessage = SMPatterns.getAnimationObjectCreateMessage(Owner.getPos(), (byte)61);
					SMain.getCommunicationHandler().SendMessage(explosionMessage);
					break;
				}
			}
		}
		for(SDebris object : SFH.Debris.getObjects()){
			if (object.getObjectState().equals(ObjectState.Active)){
				if (SGeomFunctions.intersects((SDebris)object, Owner) ){
					SVector pos = new SVector(Owner.getPos());
					if (SGeomFunctions.collide((SAsteroid)object, Owner)){
						((SDebris)object).getController().setSendCounter(0);
						SM message = SMPatterns.getObjectUpdateMessage((SDebris)object);
						SMain.getCommunicationHandler().SendMessage(message);
					}
					Owner.setObjectState(ObjectState.WaitingDelete);
					SM message = SMPatterns.getObjectDeleteMessage(Owner);
					SMain.getCommunicationHandler().SendMessage(message);
					// add explosion to client
					SM explosionMessage = SMPatterns.getAnimationObjectCreateMessage(pos, (byte)61);
					SMain.getCommunicationHandler().SendMessage(explosionMessage);
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
