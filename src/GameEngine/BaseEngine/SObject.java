package GameEngine.BaseEngine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import GameEngine.GeomEngine.SBody;
import GameEngine.GeomEngine.SHitbox;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.EffectEngine.SEffect;
import RenderingEngine.SRenderObject;

public abstract class SObject extends SUpdatable{
	protected SVector pos;
	protected SVector lookDir;
	protected SBody body;
	protected boolean posUpdated;
	protected SVector prevPos;
	
	protected LinkedList<SEffect> appliedEffects;
	
	//Initialize
	public SObject(){
		appliedEffects = new LinkedList<SEffect>();
		this.prevPos = new SVector();
		this.pos = new SVector();
		this.lookDir = new SVector(1,0);
		this.body = new SBody(this, new SHitbox(this), "res/entity/spaceshipv1.png", 1.0f, 1.0f);
	}
	// Properties
	public SVector getPos() {
		return pos;
	}
	public void setPos(SVector pos) {
		if(pos!=null)
			this.pos = pos;
			//probably the element teleported
			prevPos = new SVector(pos);
	}
	public void modifyCurrentPos(SVector pos) {
		if(pos!=null)
			this.pos = pos;
	}
	public SVector getPrevPos(){
		return prevPos;
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
	public LinkedList<SEffect> getAppliedEffects() {
		return appliedEffects;
	}
	public void addEffect(SEffect effect){
		appliedEffects.add(effect);
	}
	public void removeEffect(Object effect){
		ListIterator<SEffect> iter = appliedEffects.listIterator();
		while(iter.hasNext()){
			SEffect object = iter.next();
		    if(object.equals(effect)){
		        iter.remove();
		        break;
		    }
		}
	}
	public boolean underEffect(byte effectType){
		for(SEffect object : appliedEffects){
			if (object.getType() == effectType)
				return true;
		}
		return false;
	}
	
	
	public SEffect getEffectById(int Id){
		for(SEffect object : appliedEffects){
			if (object.equals(Id))
				return object;
		}
		System.out.printf("Object was not found in appliedEffect of object %d, with Id: "+Id+"\n", getId().get());
		return null;
	}
	// functions
	public List<SRenderObject> getDrawables(){
		List<SRenderObject> list = new ArrayList<SRenderObject>();
		list.add(new SRenderObject(body.getTexture(), pos, lookDir.getAngle(), body.getCurrentDrawScale(), 1.0f, body.getColor(), body.get_Z()));
		return list;
	}
}
