package GameEngine.WeaponEngine;

import java.util.Random;

import GameEngine.BaseEngine.SMobile;
import GameEngine.ControlEngine.SSimpleBulletControlClient;
import GameEngine.ControlEngine.SSimpleBulletControlServer;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public class SBullet extends SMobile{
	protected SEntity owner;
	protected int numberOfBulletsAtOnce = 1;
	protected float damage;
	
	public SBullet createBullet(){
		return new SBullet(owner);
	}
	public SBullet(int ownerId, SVector pos, SVector lookdir, SVector movedir){
		//used at client side
		super();
		//TODO add SSimpleBulletControl here
		this.owner = SMain.getGameInstance().getEntityById(ownerId);
		this.getBody().setTexture("res/object/bullet/bullet.png");
		this.getBody().setScale(2.0f);
		this.pos = pos;
		this.lookDir = lookdir;
		this.moveDir = movedir;
		this.maxSpeed = 100;
		this.setController(new SSimpleBulletControlClient(this));
	}
	public SBullet(SEntity owner){
		//used at server side
		super();
		this.owner = owner;
		this.getBody().setTexture("res/object/bullet/bullet.png");
		this.getBody().setHitbox(new SHitboxSpherical(this, 5));
		this.getBody().setScale(2.0f);
		this.lookDir = new SVector(owner.getLookDir());
		Random random = new Random();
		this.pos = new SVector(owner.getPos().add(lookDir.setLength(30+random.nextFloat()*6)));
		this.maxSpeed = 100;
		this.damage = 10;
		this.moveDir = this.lookDir.setLength(this.maxSpeed).add(owner.getMoveDir());
		this.setController(new SSimpleBulletControlServer(this));
	}
	public int getNumberOfBulletsAtOnce() {
		return numberOfBulletsAtOnce;
	}
	public SEntity getOwner() {
		return owner;
	}
	public float getDamage() {
		return damage;
	}
	
}
