package GameEngine.BaseEngine;

import GameEngine.EntityEngine.SControl;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public abstract class SMobile extends SObject{
	protected SVector moveDir; // movement direction, length is speed
	protected SVector acclDir; 
	protected float maxSpeed = 30.0f;
	protected float rotSpeed = 0.0f;         
	protected float maxRotSpeed = 1.0f;
	protected float maxAcceleration = 2.0f;     
	protected float rotAcceleration = 0.0f;  
	protected float maxRotAcceleration = 1.0f;
	private SControl controller;
	
	//Initialize
	public SMobile()
	{
		super();
		moveDir = new SVector();
		acclDir = new SVector();
		controller = new SControl(this);
	}
	public SMobile(SVector pos, SVector look_dir, String texture)
	{
		super(pos, look_dir, texture);
	}
	public SMobile(SMobile m)
	{
		super(m);
		this.moveDir = new SVector(m.moveDir);
		this.acclDir = new SVector(m.acclDir);
		this.maxSpeed = m.maxSpeed;
		this.rotSpeed = m.rotSpeed;  
		this.maxRotSpeed = m.maxRotSpeed;
		this.maxAcceleration = m.maxAcceleration;
		this.rotAcceleration = m.rotAcceleration;  
		this.maxRotAcceleration = m.maxRotAcceleration;  
	}
	//get-set
	
	public float getSpeed() {
		return moveDir.l();
	}
	public void setSpeed(float speed) {
		this.moveDir = this.moveDir.setLength(speed);
	}
	public float getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public float getRotSpeed() {
		return rotSpeed;
	}
	public void setRotSpeed(float rotSpeed) {
		if (rotSpeed>maxRotSpeed){
			rotSpeed = maxRotSpeed;
		}
		this.rotSpeed = rotSpeed;
	}
	public float getMaxRotSpeed() {
		return maxRotSpeed;
	}
	public void setMaxRotSpeed(float maxRotSpeed) {
		this.maxRotSpeed = maxRotSpeed;
	}
	public float getMaxAcceleration() {
		return maxAcceleration;
	}
	public void setMaxAcceleration(float maxAcceleration) {
		this.maxAcceleration = maxAcceleration;
	}
	public float getMaxRotAcceleration() {
		return maxRotAcceleration;
	}
	public void setMaxRotAcceleration(float maxRotAcceleration) {
		this.maxRotAcceleration = maxRotAcceleration;
	}
	public SVector getMoveDir() {
		return moveDir;
	}
	public void setMoveDir(SVector moveDir) {
		if (moveDir.l()>maxSpeed){
			moveDir = moveDir.setLength(maxSpeed);
		}
		this.moveDir = moveDir;
	}
	public SVector getAcclDir() {
		return acclDir;
	}
	public void setAcclDir(SVector acclDir) {
		if (acclDir.l()>maxAcceleration){
			acclDir = acclDir.setLength(maxAcceleration);
		}
		this.acclDir = acclDir;
	}
	public float getRotAcceleration() {
		return rotAcceleration;
	}
	public void setRotAcceleration(float rotAcceleration) {
		if (rotAcceleration>maxRotAcceleration){
			rotAcceleration = maxRotAcceleration;
		}
		this.rotAcceleration = rotAcceleration;
	}
	public void setController(SControl controller){
		this.controller = controller;
	}
	public SControl getController(){
		return this.controller;
	}
	// functions
	public void Move(){
		// TODO add fps dependency
		
		moveDir = moveDir.add(acclDir.m(SMain.getDeltaRatio()));
		
		if (moveDir.l()>maxSpeed){
			moveDir = moveDir.setLength(maxSpeed);
		}
		pos = pos.add(moveDir.m(SMain.getDeltaRatio()));
		
	}
	public void Rotate(){
		rotSpeed += rotAcceleration*SMain.getDeltaRatio();
		if(Math.abs(rotSpeed)>maxRotSpeed){
			rotSpeed = maxRotSpeed;
		}
		lookDir = lookDir.rotate(rotSpeed);
	}
	
	public void update(){
		controller.Think();
	}
}
