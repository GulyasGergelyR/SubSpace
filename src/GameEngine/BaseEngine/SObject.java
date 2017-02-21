package GameEngine.BaseEngine;

import java.util.ArrayList;
import java.util.List;

import GameEngine.SIdentifiable;
import GameEngine.GeomEngine.SBody;
import GameEngine.GeomEngine.SHitbox;
import GameEngine.GeomEngine.SVector;
import RenderingEngine.SRenderObject;

public abstract class SObject extends SIdentifiable {
	protected SVector pos;
	protected SVector lookDir;
	protected SBody body;
	protected boolean posUpdated;
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
	
	//Initialize
	public SObject()
	{
		this.pos = new SVector();
		this.lookDir = new SVector(1,0);
		this.body = new SBody(this, new SHitbox(this), "res/entity/spaceshipv1.png", 1.0f, 1.0f);
	}
	public SObject(SVector pos, SVector lookDir, String texture)
	{
		this.pos = pos;
		this.lookDir = lookDir;
		this.body = new SBody(this, new SHitbox(this), texture, 1.0f, 1.0f);
	}
	public SObject(SObject o)
	{
		this.pos = o.pos;
		this.lookDir = o.lookDir;
		this.body = new SBody(this, o.getBody().getHitbox().SHCopy(this), o.getBody().getTexture(), o.getBody().getScale(), o.getBody().getDrawScale());
	}
	// Properties
	public SVector getPos() {
		return pos;
	}
	public void setPos(SVector pos) {
		if(pos!=null)
			this.pos = pos;
	}
	public boolean equals(SObject o){
		return this.Id.equals(o.Id);
	}
	public SVector getLookDir() {
		return lookDir;
	}
	public void setLookDir(SVector lookDir) {
		if(lookDir!=null)
			this.lookDir = lookDir;
	}
	public boolean IsPosUpdated(){
		return posUpdated;
	}
	public void setPosUpdated(){
		posUpdated = true;
	}
	public SBody getBody() {
		return body;
	}
	public void setBody(SBody body) {
		this.body = body;
	}
	// functions
	public List<SRenderObject> getDrawables(){
		List<SRenderObject> list = new ArrayList<SRenderObject>();
		list.add(new SRenderObject(body.getTexture(), pos, lookDir.getAngle(), body.getCurrentDrawScale(), 1.0f, body.getColor(), body.get_Z()));
		return list;
	}
	public void update(){}
}
