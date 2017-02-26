package GameEngine.EntityEngine;

import java.util.ListIterator;

import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SUpdatable;
import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.SFactory;
import GameEngine.PlayerEngine.SPlayer;
import Main.SMain;
import WebEngine.ComEngine.SNode;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SEntityFactory extends SFactory<SEntity> {
	public SEntityFactory(){
		super("Entity factory", (byte)10);
	}
	
	public void createEntityAtServer(int id){
		SNode Node = SMain.getCommunicationHandler().getNodeById(id);
		SEntity entity = new SEntity(Node.getPlayer());
		addObject(entity);
		SFH.Players.addObject(Node.getPlayer());
	}
	
	@Override
	public void UpdateObjects() {
		ListIterator<SEntity> iter = objects.listIterator();
		while(iter.hasNext()){
			SEntity entity = iter.next();
			if(entity.shouldBeDeleted()){
		        iter.remove();
		        SFH.Players.removeObjectFromList(entity.getId().get());
			}else if(entity.getObjectState().equals(ObjectState.Initialization)){
			        for(SPlayer player : SFH.Players.getObjects()){
			        	if (!player.equals(entity)&&!player.getEntity().shouldBeDeleted()){
			        		SM message = SMPatterns.getEntityCreateMessage(player);
			        		SMain.getCommunicationHandler().SendMessageToNode(message, entity.getId().get());
					        SM messageEntityState = SMPatterns.getEntityUpdateStateMessage(player.getEntity());
				        	SMain.getCommunicationHandler().SendMessageToNode(messageEntityState, entity.getId().get());
			        	}
			        }
			        for(SObject object : objects){
			        	if (object.isActive()){
			        		SM message = SMPatterns.getObjectCreateMessage(object);
			        		SMain.getCommunicationHandler().SendMessageToNode(message, entity.getId().get());
			        	}
			        }
			        for(SObject object : SFH.Debris.getObjects()){
			        	if (object.isActive()){
			        		SM message = SMPatterns.getObjectCreateMessage(object);
			        		SMain.getCommunicationHandler().SendMessageToNode(message, entity.getId().get());
			        	}
			        }
			        for(SObject object : SFH.PowerUps.getObjects()){
			        	if (object.isActive()){
				        	SM message = SMPatterns.getObjectCreateMessage(object);
				        	SMain.getCommunicationHandler().SendMessageToNode(message, entity.getId().get());
			        	}
			        }
			        for(SUpdatable object : SFH.Effects.getObjects()){
			        	if (object.isActive()){
			        		SM message = SMPatterns.getObjectCreateMessage(object);
				        	SMain.getCommunicationHandler().SendMessageToNode(message, entity.getId().get());
			        	}
			        }
			        entity.setObjectState(ObjectState.Active);
			        SM message = SMPatterns.getEntityUpdateStateMessage(entity);
		        	SMain.getCommunicationHandler().SendMessage(message);
		    }else if (entity.isActive()){
		    		entity.update();
		    		if(entity.shouldBeDeleted()){
				        iter.remove();
				        SFH.Players.removeObjectFromList(entity.getId().get());
			    }
		    }
		}
	}
}
