package GameEngine.BaseEngine;

import GameEngine.GeomEngine.SVector;
import Main.SMain;

public abstract class SMobile extends SObject{
	private SVector moveDir;               // actual movement direction
	private float speed = 0.0f;            // point/frame
	private float maxSpeed = 1.0f;
	private float rotSpeed = 0.0f;         // degree/frame
	private float maxRotSpeed = 1.0f;
	private float acceleration = 0.0f;     // point/frame^2
	private float rotAcceleration = 0.0f;  // degree/frame^2
	
	//Initialize
	public SMobile()
	{
		super();
		moveDir = new SVector();
	}
	public SMobile(SVector pos, SVector look_dir, Object texture)
	{
		super(pos, look_dir, texture);
	}
	public SMobile(SMobile m)
	{
		super(m);
		this.speed = m.speed;
		this.maxSpeed = m.maxSpeed;
		this.rotSpeed = m.rotSpeed;  
		this.maxRotSpeed = m.maxRotSpeed;
		this.acceleration = m.acceleration;
		this.rotAcceleration = m.rotAcceleration;  
	}
	//get-set
	
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(float max_speed) {
		this.maxSpeed = max_speed;
	}
	public float getRotSpeed() {
		return rotSpeed;
	}
	public void setRotSpeed(float rot_speed) {
		this.rotSpeed = rot_speed;
	}
	public float getMaxRotSpeed() {
		return maxRotSpeed;
	}
	public void setMaxRotSpeed(float rot_max_speed) {
		this.maxRotSpeed = rot_max_speed;
	}
	public SVector getMoveDir() {
		return moveDir;
	}
	public void setMoveDir(SVector moveDir) {
		this.moveDir = moveDir.norm();
	}
	public float getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}
	public float getRotAcceleration() {
		return rotAcceleration;
	}
	public void setRotAcceleration(float rotAcceleration) {
		this.rotAcceleration = rotAcceleration;
	}
	public void Move(){
		// TODO add fps dependency
		speed += acceleration*SMain.getDeltaRatio();
		pos = pos.add(moveDir.m(speed*SMain.getDeltaRatio()));
		
		rotSpeed += rotAcceleration*SMain.getDeltaRatio();
		lookDir = lookDir.rotate(rotSpeed);
	}
}
