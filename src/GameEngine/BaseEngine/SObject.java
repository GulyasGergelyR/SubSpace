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
	protected ObjectState objectState = ObjectState.Active;
	
	//Initialize
	public SObject()
	{
		this.pos = new SVector();
		this.lookDir = new SVector(1,0);
		this.body = new SBody(this, new SHitbox(this), "res/entity/spaceshipv1.png", 1.0f);
	}
	public SObject(SVector pos, SVector lookDir, String texture)
	{
		this.pos = pos;
		this.lookDir = lookDir;
		this.body = new SBody(this, new SHitbox(this), texture, 1.0f);
	}
	public SObject(SObject o)
	{
		this.pos = o.pos;
		this.lookDir = o.lookDir;
		this.body = new SBody(this, o.getBody().getHitbox().SHCopy(this), o.getBody().getTexture(), o.getBody().getScale());
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
	public ObjectState getObjectState() {
		return objectState;
	}
	public void setObjectState(ObjectState objectState) {
		this.objectState = objectState;
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
		list.add(new SRenderObject(body.getTexture(), pos, lookDir.getAngle(), body.getScale(), 1.0f, body.getColor()));
		return list;
	}	
	public void update(){}
}
