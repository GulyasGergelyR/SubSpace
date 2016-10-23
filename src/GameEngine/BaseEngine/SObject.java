package GameEngine.BaseEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.newdawn.slick.opengl.Texture;

import GameEngine.SResLoader;
import GameEngine.GeomEngine.SVector;
import RenderingEngine.SRenderObject;

public abstract class SObject {
	protected SVector pos;
	protected SVector lookDir;
	protected String texture;
	protected float scale;
	private UUID Id = UUID.randomUUID();
	// TODO add hitbox

	
	
	//Initialize
	public SObject()
	{
		this.pos = new SVector();
		this.lookDir = new SVector();
		this.texture = "res/entity/spaceshipv1.png";
		this.scale = 1.0f;
		this.Id = UUID.randomUUID();
	}
	public SObject(SVector pos, SVector lookDir, String texture)
	{
		this.pos = pos;
		this.lookDir = lookDir;
		this.Id = UUID.randomUUID();
		this.scale = 1.0f;
		this.texture = texture;
	}
	public SObject(SObject o)
	{
		this.pos = o.pos;
		this.lookDir = o.lookDir;
		this.texture = o.texture;
		this.Id = o.Id;
	}
	// Properties
	public SVector getPos() {
		return pos;
	}
	public void setPos(SVector pos) {
		this.pos = pos;
	}
	public UUID getId() {
		return Id;
	}
	public void setId(UUID id) {
		Id = id;
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
		this.lookDir = lookDir;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	// functions
	public List<SRenderObject> Draw(){
		List<SRenderObject> list = new ArrayList<SRenderObject>();
		list.add(new SRenderObject(texture, pos, lookDir.getAngle(), scale, 1.0f));
		return list;
	}	
	
}
