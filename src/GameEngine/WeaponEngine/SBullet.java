package GameEngine.WeaponEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.ControlEngine.SSimpleBulletControlServer;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SBullet extends SMobile{
	protected SEntity owner;
	protected int numberOfBulletsAtOnce = 1;
	
	protected SVector origin;
	protected int maxLifeTime = 100;
	protected int currentLifeTime = 0;
	protected int maxLifeDistance = 100;
	
	public SBullet createBullet(){
		return new SBullet(owner);
	}
	public SBullet(int ownerId){
		//used at client side
		super();
		//TODO add SSimpleBulletControl here
		this.owner = SMain.getGameInstance().getEntityById(ownerId);
		this.texture = "res/object/bullet/bullet.png";
		this.pos = new SVector(owner.getPos());
		origin = new SVector(pos);
		this.lookDir = new SVector(owner.getLookDir());
		this.maxSpeed = 200;
		this.moveDir = this.lookDir.setLength(70).add(owner.getMoveDir());
	}
	public SBullet(SEntity owner){
		//used at server side
		super();
		//TODO add SSimpleBulletControl here
		this.owner = owner;
		this.texture = "res/object/bullet/bullet.png";
		this.pos = new SVector(owner.getPos());
		origin = new SVector(pos);
		this.lookDir = new SVector(owner.getLookDir());
		this.maxSpeed = 200;
		this.moveDir = this.lookDir.setLength(70).add(owner.getMoveDir());
		this.setController(new SSimpleBulletControlServer(this));
	}
	public int getNumberOfBulletsAtOnce() {
		return numberOfBulletsAtOnce;
	}
	public SEntity getOwner() {
		return owner;
	}
}
