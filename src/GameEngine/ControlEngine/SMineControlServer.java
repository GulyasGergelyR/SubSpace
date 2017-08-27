package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SEntity.PlayerGameState;
import GameEngine.GeomEngine.SInteraction;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.DebrisEngine.SDebrisFactory;
import GameEngine.ObjectEngine.DebrisEngine.SMine;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SMineControlServer extends SControlServer<SMine>{
	SEntity target = null;
	boolean hadTargetBefore = false;
	final static float detectDistance = 900;
	public SMineControlServer(SMine mobile){
		super(mobile);
	}
	@Override
	protected void Think() {
		float distanceToClosestEntity = detectDistance;
		SVector direction = new SVector();
		
		if (target != null){
			if (!target.isActive() || !target.isAlive()){
				target = null;
				distanceToClosestEntity = detectDistance;
			} else {
				distanceToClosestEntity = target.getPos().d(Owner.getPos());
				direction = target.getPos().sub(Owner.getPos());
			}
		}
		
		for(SEntity entity : SFH.Entities.getObjects()){
			if (!entity.isActive() || !entity.isAlive()){
				continue;
			}
			float currentDistance = entity.getPos().d(Owner.getPos());
			if (currentDistance < distanceToClosestEntity){
				target = entity;
				hadTargetBefore = true;
				distanceToClosestEntity = currentDistance;
				direction = entity.getPos().sub(Owner.getPos());
			}
		}
		if (target != null){
			Owner.setAcclDir(direction.setLength(Owner.getMaxAcceleration()));
			setSendCounter(0);
			SM message = SMPatterns.getObjectUpdateMessage(Owner);
			SMain.getCommunicationHandler().SendMessage(message);
		} else {
			Owner.setAcclDir(new SVector());
			if (hadTargetBefore){
				Owner.setSpeed(20);
				setSendCounter(0);
				SM message = SMPatterns.getObjectUpdateMessage(Owner);
				SMain.getCommunicationHandler().SendMessage(message);
			}
		}
		
		for(SEntity entity : SFH.Entities.getObjects()){
			if (entity.getObjectState().equals(ObjectState.Active) &&
					entity.getPlayerGameState().equals(PlayerGameState.Alive)){
				SInteraction interaction = new SInteraction(entity, Owner);
				if (interaction.IsHappened()){
					Owner.getController().setSendCounter(0);
					SM message = SMPatterns.getObjectUpdateMessage(Owner);
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
				SFH.Debris.deletedDebris(SDebrisFactory.Mine);
				}
			}
		}
}
