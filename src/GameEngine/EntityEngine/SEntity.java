package GameEngine.EntityEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;

import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.SPlayer;
import GameEngine.SPlayer.PlayerState;
import GameEngine.ControlEngine.SControl;
import GameEngine.ControlEngine.SControlClient;
import GameEngine.ControlEngine.SHumanControlClient;
import GameEngine.ControlEngine.SHumanControlServer;
import GameEngine.GeomEngine.SVector;
import GameEngine.WeaponEngine.SWeapon;
import Main.SMain;
import RenderingEngine.SRenderObject;
import WebEngine.ComEngine.SCommunicationHandler.UDPRole;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SEntity extends GameEngine.BaseEngine.SMobile{
	protected SPlayer player;
	protected float life;
	protected float maxLife = 150;
	protected List<SWeapon> weapons;
	protected SWeapon activeWeapon;
	
	public SEntity(SPlayer player){
		super();
		this.pos = new SVector(250.0f,250.0f);
		this.getBody().setTexture("res/entity/spaceshipv3.png");
		this.getBody().setScale(0.5f);
		this.getBody().setDrawScale(0.1f);
		this.getBody().setHitbox(new SHitboxSpherical(this, 768/2*getBody().getDrawScale()));
		Random random = new Random();
		this.getBody().setColor(new Color(128+random.nextInt(127), 128+random.nextInt(127), 128+random.nextInt(127), 0));
		this.player = player;
		this.life = 100;
		player.setEntity(this);
		// Add weapons
		weapons = new ArrayList<SWeapon>();
		SWeapon weapon = new SWeapon(this);
		activeWeapon = weapon;
		if (SMain.IsServer()){
			setObjectState(ObjectState.Initialization);
		}
		else{
			setObjectState(ObjectState.Ghost);
		}
		
		if (player.getPlayerState().equals(PlayerState.local)){
			System.out.println("Created local player at: "+SMain.getCommunicationHandler().getUDPRole());
			this.setController(new SHumanControlClient(this));
		}else if(SMain.getCommunicationHandler().getUDPRole().equals(UDPRole.Server)){
			System.out.println("Created lan player at server");
			this.setController(new SHumanControlServer(this));
		} else {
			System.out.println("Created lan player at client");
			this.setController(new SControlClient(this));
		}
	}
		
	@Override
	public List<SRenderObject> getDrawables() {
		//TODO Add movement and life specific drawings
		List<SRenderObject> list = super.getDrawables();
		return list;
	}
	public void tryToFire(){
		if (!activeWeapon.tryIt())
			activeWeapon.coolIt();
	}
	
	public boolean gotHit(float damage){
		this.life -= damage;
		if (this.life <= 0){
			SM explosionMessage = SMPatterns.getAnimationObjectCreateMessage(getPos(), (byte)60);
			SMain.getCommunicationHandler().SendMessage(explosionMessage);
			this.life = 100;
			Random random = new Random();
			this.pos = new SVector(random.nextFloat()*4000-2000,random.nextFloat()*4000-2000);
			this.moveDir = new SVector();
			this.acclDir = new SVector();
			this.lookDir = new SVector(1,0);
			this.player.addDeath(1);
			return true;
		}
		else {
			return false;
		}
	}
	public float getLife(){
		return life;
	}
	public void setLife(float life){
		if (life > maxLife)
			this.life = maxLife;
		else this.life = life;
	}
	public float getMaxLife() {
		return maxLife;
	}
	public void setMaxLife(float maxLife) {
		this.maxLife = maxLife;
	}

	public SPlayer getPlayer() {
		return player;
	}
	
}
