package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SObject.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SGeomFunctions;
import GameEngine.WeaponEngine.SBullet;
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
		for(SEntity entity : SMain.getGameInstance().getEntities()){
			SEntity bulletOwner = ((SBullet)Owner).getOwner();
			if (!entity.equals(bulletOwner)){
				if (SGeomFunctions.intersects(entity, Owner)){
					if (entity.gotHit(((SBullet)Owner).getDamage()))
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
		if (Owner.getObjectState().equals(ObjectState.Active)){
			if (currentLifeTime < maxLifeTime){
				currentLifeTime++;
			}
			else{
				// Delete this
				Owner.setObjectState(ObjectState.WaitingDelete);
				SM message = SMPatterns.getObjectDeleteMessage(Owner);
				SMain.getCommunicationHandler().SendMessage(message);
				
				SM explosionMessage = SMPatterns.getAnimationObjectCreateMessage(Owner.getPos(), (byte)61);
				SMain.getCommunicationHandler().SendMessage(explosionMessage);
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
