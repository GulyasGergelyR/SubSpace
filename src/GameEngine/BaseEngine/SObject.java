package GameEngine.BaseEngine;

import GameEngine.GeomEngine.SVector;

public abstract class SObject {
	private SVector pos;
	
	private SVector look_dir;
	// TODO add sprite
	// TODO add hitbox
	
	private int Id;
	
	//Initialize
	public SObject()
	{
		this.pos = new SVector();
		this.look_dir = new SVector();
	}
	public SObject(SVector pos, SVector look_dir)
	{
		this.pos = pos;
		this.look_dir = look_dir;
	}
	public SObject(SObject o)
	{
		this.pos = o.pos;
		this.look_dir = o.look_dir;
	}
	// get-set
	public SVector getPos() {
		return pos;
	}
	public void setPos(SVector pos) {
		this.pos = pos;
	}
	
	public SVector getLook_dir() {
		return look_dir;
	}
	public void setLook_dir(SVector look_dir) {
		this.look_dir = look_dir;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	
	// functions
	
	
	
}
