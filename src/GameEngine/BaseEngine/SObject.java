package GameEngine.BaseEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import GameEngine.GeomEngine.SBody;
import GameEngine.GeomEngine.SHitbox;
import GameEngine.GeomEngine.SVector;
import RenderingEngine.SRenderObject;

public abstract class SObject {
	protected SVector pos;
	protected SVector lookDir;
	protected SBody body;
	protected UUID Id = UUID.randomUUID();
	protected boolean posUpdated;
	
	public enum OjectState{
		Active, Ghost, Invisible, OnDeathRaw
	}
	protected OjectState objectState = OjectState.Active;
	
	//Initialize
	public SObject()
	{
		this.pos = new SVector();
		this.lookDir = new SVector(1,0);
		this.body = new SBody(this, new SHitbox(this), "res/entity/spaceshipv1.png", 1.0f);
		this.Id = UUID.randomUUID();
	}
	public SObject(SVector pos, SVector lookDir, String texture)
	{
		this.pos = pos;
		this.lookDir = lookDir;
		this.Id = UUID.randomUUID();
		this.body = new SBody(this, new SHitbox(this), texture, 1.0f);
	}
	public SObject(SObject o)
	{
		this.pos = o.pos;
		this.lookDir = o.lookDir;
		this.body = new SBody(this, o.getBody().getHitbox().SHCopy(this), o.getBody().getTexture(), o.getBody().getScale());
		this.Id = o.Id;
	}
	// Properties
	public SVector getPos() {
		return pos;
	}
	public void setPos(SVector pos) {
		if(pos!=null)
			this.pos = pos;
	}
	public UUID getId() {
		return Id;
	}
	public void setId(UUID id) {
		Id = id;
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
	public OjectState getObjectState() {
		return objectState;
	}
	public void setObjectState(OjectState objectState) {
		this.objectState = objectState;
	}
	// functions
	public List<SRenderObject> getDrawables(){
		List<SRenderObject> list = new ArrayList<SRenderObject>();
		list.add(new SRenderObject(body.getTexture(), pos, lookDir.getAngle(), body.getScale(), 1.0f));
		return list;
	}	
	
}
