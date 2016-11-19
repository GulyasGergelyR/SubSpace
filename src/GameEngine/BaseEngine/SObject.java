package GameEngine.BaseEngine;

import java.util.ArrayList;
import java.util.List;

import GameEngine.SId;
import GameEngine.SIdentifiable;
import GameEngine.GeomEngine.SVector;
import RenderingEngine.SRenderObject;

public abstract class SObject extends SIdentifiable {
	protected SVector pos;
	protected SVector lookDir;
	protected String texture;
	protected float scale;
	protected boolean posUpdated;
	
	// TODO add hitbox

	//Initialize
	public SObject()
	{
		this.pos = new SVector();
		this.lookDir = new SVector(1,0);
		this.texture = "res/entity/spaceshipv1.png";
		this.scale = 1.0f;
	}
	public SObject(SVector pos, SVector lookDir, String texture)
	{
		this.pos = pos;
		this.lookDir = lookDir;
		this.scale = 1.0f;
		this.texture = texture;
	}
	public SObject(SObject o)
	{
		this.pos = o.pos;
		this.lookDir = o.lookDir;
		this.texture = o.texture;
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
	public void setTexture(String s){
		this.texture = s;
	}
	public String getTexture(){
		return texture;
	}
	public SVector getLookDir() {
		return lookDir;
	}
	public void setLookDir(SVector lookDir) {
		if(lookDir!=null)
			this.lookDir = lookDir;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	public boolean IsPosUpdated(){
		return posUpdated;
	}
	public void setPosUpdated(){
		posUpdated = true;
	}
	// functions
	public List<SRenderObject> getDrawables(){
		List<SRenderObject> list = new ArrayList<SRenderObject>();
		list.add(new SRenderObject(texture, pos, lookDir.getAngle(), scale, 1.0f));
		return list;
	}	
	
}
