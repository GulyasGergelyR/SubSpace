package GameEngine.BaseEngine;

import GameEngine.ControlEngine.SControl;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public abstract class SMobile extends SObject{
	protected SVector moveDir; // movement direction, length is speed
	protected SVector acclDir; 
	protected SVector aimLookDir;
	protected float maxSpeed = 40.0f;
	protected float maxAcceleration = 10.0f;     
	protected float rotSpeed = 0.0f;         
	protected float maxRotSpeed = 40.0f;
	protected float rotAcceleration = 0.0f;  
	protected float maxRotAcceleration = 6.0f;
	private SControl<SMobile> controller;
	
	//Initialize
	public SMobile(){
		super();
		moveDir = new SVector();
		acclDir = new SVector();
		aimLookDir = new SVector(lookDir);
	}
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
		if (Math.abs(rotSpeed)>maxRotSpeed){
			rotSpeed = maxRotSpeed*Integer.signum((int)rotSpeed);
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
	public SVector getAimLookDir() {
		return aimLookDir;
	}
	public void setAimLookDir(SVector aimLookDir) {
		this.aimLookDir = aimLookDir;
	}
	public SVector getMoveDir() {
		return moveDir;
	}
	public void setMoveDir(SVector moveDir) {
		if(moveDir!=null){
			if (moveDir.l()>maxSpeed){
				moveDir = moveDir.setLength(maxSpeed);
			}
			this.moveDir = moveDir;
		}
	}
	public SVector getAcclDir() {
		return acclDir;
	}
	public void setAcclDir(SVector acclDir) {
		if(acclDir!=null){
			if (acclDir.l()>maxAcceleration){
				acclDir = acclDir.setLength(maxAcceleration);
			}
			this.acclDir = acclDir;
		}
	}
	public float getRotAcceleration() {
		return rotAcceleration;
	}
	public void setRotAcceleration(float rotAcceleration) {
		if (Math.abs(rotAcceleration)>maxRotAcceleration){
			rotAcceleration = maxRotAcceleration*Integer.signum((int)rotAcceleration);
		}
		this.rotAcceleration = rotAcceleration;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setController(SControl controller){
		this.controller = controller;
	}
	@SuppressWarnings("rawtypes")
	public SControl getController(){
		return this.controller;
	}
	// functions
	public void Move(){
			prevPos = new SVector(pos);
			moveDir = moveDir.add(acclDir.m(SMain.getDeltaRatio()));
			if (moveDir.l()>maxSpeed){
				moveDir = moveDir.setLength(maxSpeed);
			}
			pos = pos.add(moveDir.m(SMain.getDeltaRatio()));
	}
	public void Rotate(){
		//TODO correct rotation
		//rotSpeed += rotAcceleration*SMain.getDeltaRatio();
		rotSpeed = maxRotSpeed*Integer.signum((int)rotAcceleration);
		if(Math.abs(rotSpeed)>maxRotSpeed){
			rotSpeed = maxRotSpeed*Integer.signum((int)rotSpeed);
		}
		float angle = lookDir.getAbsAngleBetween(aimLookDir);
		if(angle<Math.abs(rotSpeed*SMain.getDeltaRatio()))//rotSpeed*SMain.getDeltaRatio())
			lookDir = new SVector(aimLookDir);
		else lookDir = lookDir.rotate(rotSpeed*SMain.getDeltaRatio());
	}
	@Override
	public void update(){
		controller.ThinkAndAct();
		posUpdated = false;
	}
}
