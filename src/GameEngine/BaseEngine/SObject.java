package GameEngine.BaseEngine;

import java.util.UUID;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.newdawn.slick.opengl.Texture;
import org.omg.CosNaming.IstringHelper;

import GameEngine.SResLoader;
import GameEngine.GeomEngine.SVector;
import RenderingEngine.SRenderObject;

public abstract class SObject {
	protected SVector pos;
	protected SVector lookDir;
	protected Texture texture;
	private UUID Id = UUID.randomUUID();
	// TODO add hitbox

	
	
	//Initialize
	public SObject()
	{
		this.pos = new SVector();
		this.lookDir = new SVector();
		this.texture = SResLoader.getTexture("res/default.png");
		this.Id = UUID.randomUUID();
	}
	public SObject(SVector pos, SVector lookDir, Object texture)
	{
		this.pos = pos;
		this.lookDir = lookDir;
		this.Id = UUID.randomUUID();
		if (texture instanceof String){
			this.texture = SResLoader.getTexture((String)texture);
		}
		else{
			this.texture = (Texture)texture;
		}
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
	
	public SVector getLook_dir() {
		return lookDir;
	}
	public void setLook_dir(SVector lookDir) {
		this.lookDir = lookDir;
	}
	public UUID getId() {
		return Id;
	}
	public void setId(UUID id) {
		Id = id;
	}
	public void setTexture(String s){
		this.texture = SResLoader.getTexture(s);
	}
	public void setTexture(Texture t){
		this.texture = t;
	}
	public Texture getTexture(){
		return texture;
	}
	
	// functions
	public SRenderObject Draw(){
		return new SRenderObject(texture, pos, lookDir.getAngle());
	}
	
	
}
