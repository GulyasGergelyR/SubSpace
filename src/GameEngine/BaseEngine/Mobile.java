package GameEngine.BaseEngine;

import GameEngine.GeomEngine.SVector;

public abstract class Mobile extends SObject{
	private SVector pos_before;
	private float speed;
	private float max_speed;
	private float rot_speed;
	private float rot_max_speed;
	
	//Initialize
	public Mobile()
	{
		super();
		pos_before = new SVector();
		this.speed = 0.0f;
		this.max_speed = 0.0f;
		this.rot_speed = 0.0f;
		this.rot_max_speed = 0.0f;
	}
	public Mobile(SVector pos, SVector pos_before, SVector look_dir, float speed, float max_speed, float rot_speed, float rot_max_speed)
	{
		super(pos, look_dir);
		this.pos_before = pos_before;
		this.speed = speed;
		this.max_speed = max_speed;
		this.rot_speed = rot_speed;
		this.rot_max_speed = rot_max_speed;
	}
	public Mobile(Mobile m)
	{
		super(m);
		this.pos_before = m.pos_before;
		this.speed = m.speed;
		this.max_speed = m.max_speed;
		this.rot_speed = m.rot_speed;
		this.rot_max_speed = m.rot_max_speed;
	}
	//get-set
	public SVector getPos_before() {
		return pos_before;
	}
	public void setPos_before(SVector pos_before) {
		this.pos_before = pos_before;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getMax_speed() {
		return max_speed;
	}
	public void setMax_speed(float max_speed) {
		this.max_speed = max_speed;
	}
	public float getRot_speed() {
		return rot_speed;
	}
	public void setRot_speed(float rot_speed) {
		this.rot_speed = rot_speed;
	}
	public float getRot_max_speed() {
		return rot_max_speed;
	}
	public void setRot_max_speed(float rot_max_speed) {
		this.rot_max_speed = rot_max_speed;
	}
	//functions
	public void Move_by(SVector v)
	{
		
	}
	public void Rotate_by(SVector v)
	{
		
	}
}
