package GameEngine.BaseEngine;

import GameEngine.SIdentifiable;

public class SUpdatable extends SIdentifiable{
	public SUpdatable(){
		super();
	}
	public SUpdatable(int Id){
		super(Id);
	}
	
	public enum ObjectState{
		Active, Ghost, Invisible, WaitingDelete, Initialization
	}
	public byte getObjectStateId(){
		if (objectState.equals(ObjectState.Active))
			return 5;
		else if (objectState.equals(ObjectState.Ghost))
			return 4;
		else if (objectState.equals(ObjectState.Invisible))
			return 3;
		else if (objectState.equals(ObjectState.WaitingDelete))
			return 1;
		else if (objectState.equals(ObjectState.Initialization))
			return 2;
		return 0;
	}
	public ObjectState getObjectState() {
		return objectState;
	}
	public void setObjectState(ObjectState objectState) {
		this.objectState = objectState;
	}
	public void setObjectState(byte state) {
		if (state == 5)
			objectState = ObjectState.Active;
		if (state == 4)
			objectState = ObjectState.Ghost;
		if (state == 3)
			objectState = ObjectState.Invisible;
		if (state == 1)
			objectState = ObjectState.WaitingDelete;
		if (state == 2)
			objectState = ObjectState.Initialization;
		
	}
	protected ObjectState objectState = ObjectState.Active;
	
	public void update(){}
	public boolean shouldBeDeleted(){
		return objectState.equals(ObjectState.WaitingDelete);
	}
	public boolean isActive(){
		return objectState.equals(ObjectState.Active);
	}
	public void kill(){
		
	}
}
