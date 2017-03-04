package GameEngine.WeaponEngine;

import java.util.Random;

import GameEngine.SResLoader;
import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;
import GameEngine.ControlEngine.SSimpleBulletControlClient;
import GameEngine.ControlEngine.SSimpleBulletControlServer;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFH;
import GameEngine.PlayerEngine.SPlayer;

public class SBullet extends SMobile{
	protected SEntity owner;
	protected int numberOfBulletsAtOnce = 1;
	protected float damage;
	
	public SBullet createBullet(){
		return new SBullet(owner, this);
	}
	public SBullet(int ownerId, SVector pos, SVector lookdir, SVector movedir){
		//used at client side
		super();
		//TODO add SSimpleBulletControl here
		this.owner = SFH.Entities.getObjectById(ownerId);
		this.getBody().setTexture("res/object/bullet/yellowbullet.png");
		this.getBody().setScale(this.owner.getActiveWeapon().getBaseBullet().getBody().getScale());
		this.getBody().setColor(this.owner.getBody().getColor());
		this.setPos(pos);
		this.lookDir = lookdir;
		this.moveDir = movedir;
		this.maxSpeed = Specifications.bulletSpeed;
		this.setController(new SSimpleBulletControlClient(this));
		
		SPlayer localPlayer = SFH.Players.getLocalPlayer();
		if (localPlayer.getEntity().getObjectState().equals(ObjectState.Active))
		{
			SVector playerPos = localPlayer.getEntity().getPos();
			float dist = playerPos.d(pos);
			float maxDistance = 4000;
			if (dist < maxDistance){
				float maxVolume = 0.05f;
				float volume = (maxDistance-dist)*maxVolume/maxDistance;
				SResLoader.getAudio("res/audio/single_laser_shot.wav").playAsSoundEffect(1.0f, volume, false);
			}
		}
	}
	public SBullet(SEntity owner, SBullet bullet){
		//used at server side
		super();
		this.owner = owner;
		this.getBody().setTexture("res/object/bullet/yellowbullet.png");
		this.getBody().setHitbox(new SHitboxSpherical(this, 10));
		this.getBody().setDrawScale(0.25f);
		this.getBody().setScale(bullet.getBody().getScale());
		this.getBody().setMass(bullet.getBody().getMass());
		this.lookDir = new SVector(owner.getLookDir());
		Random random = new Random();
		this.setPos(new SVector(owner.getPos().add(lookDir.setLength(30+random.nextFloat()*6))));
		this.maxSpeed = bullet.getMaxSpeed();
		this.damage = bullet.getDamage();
		this.moveDir = this.lookDir.setLength(this.maxSpeed);//.add(owner.getMoveDir());
		this.setController(new SSimpleBulletControlServer(this));
	}
	public SBullet(SEntity owner){
		//used at server side
		super();
		this.owner = owner;
		this.getBody().setTexture("res/object/bullet/yellowbullet.png");
		this.getBody().setHitbox(new SHitboxSpherical(this, 10));
		this.getBody().setDrawScale(0.25f);
		this.getBody().setScale(0.25f);
		this.getBody().setMass(0.03f);
		this.lookDir = new SVector(owner.getLookDir());
		Random random = new Random();
		this.setPos(new SVector(owner.getPos().add(lookDir.setLength(30+random.nextFloat()*6))));
		this.maxSpeed = Specifications.bulletSpeed;
		this.damage = Specifications.bulletDamage;
		this.moveDir = this.lookDir.setLength(this.maxSpeed);//.add(owner.getMoveDir());
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
	public void setDamage(float damage) {
		this.damage = damage;
	}
	
}
