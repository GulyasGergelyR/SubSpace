package GameEngine.EntityEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;

import GameEngine.SPlayer;
import GameEngine.Specifications;
import GameEngine.SPlayer.PlayerType;
import GameEngine.ControlEngine.SControlClient;
import GameEngine.ControlEngine.SHumanControlClient;
import GameEngine.ControlEngine.SHumanControlServer;
import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.GeomEngine.SVector;
import GameEngine.WeaponEngine.SWeapon;
import Main.SMain;
import RenderingEngine.SRenderObject;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SEntity extends GameEngine.BaseEngine.SMobile{
	protected SPlayer player;
	protected float life;
	protected float shield;
	protected float maxLife = Specifications.entityLife;
	protected float maxShield = Specifications.entityShield;
	protected float shieldRechargeRate = 0.2f;
	protected float shieldRechargeDelay = 0;
	protected float maxShieldRechargeDelay = 120; 
	
	protected boolean undamagable = false;
	
	protected List<SWeapon> weapons;
	protected SWeapon activeWeapon;
	
	public enum PlayerGameState{
		Alive, Dead, Respawning
	}
	protected PlayerGameState playerGameState = PlayerGameState.Dead;
	
	public SEntity(SPlayer player){
		super();
		this.pos = new SVector(250.0f,250.0f);
		this.getBody().setTexture("res/entity/spaceshipv3.png");
		this.getBody().setScale(0.8f);
		this.getBody().setDrawScale(0.1f);
		this.getBody().setHitbox(new SHitboxSpherical(this, 768/2*getBody().getDrawScale()));
		this.getBody().setMass(Specifications.entityMass);
		Random random = new Random();
		this.getBody().setColor(new Color(128+random.nextInt(127), 128+random.nextInt(127), 128+random.nextInt(127), 0));
		this.player = player;
		this.life = maxLife;
		this.shield = maxShield;
		player.setEntity(this);
		// Add weapons
		weapons = new ArrayList<SWeapon>();
		SWeapon weapon = new SWeapon(this);
		weapons.add(weapon);
		activeWeapon = weapon;
		if (SMain.IsServer()){
			setObjectState(ObjectState.Initialization);
		}
		else{
			setObjectState(ObjectState.Ghost);
		}
		
		if (player.getPlayerType().equals(PlayerType.local)){
			System.out.println("Created local player at: "+SMain.getAppRole());
			this.setController(new SHumanControlClient(this));
		}else if(SMain.IsServer()){
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
		List<SRenderObject> list = new ArrayList<SRenderObject>();
		if (playerGameState.equals(PlayerGameState.Alive)){
			list.add(new SRenderObject(body.getTexture(), pos, lookDir.getAngle(), body.getCurrentDrawScale(), 1.0f, body.getColor(), 2.0f));
			//Add health bar
			SVector leftBottom = new SVector((maxShield-shield)/maxShield*0.5f,0.0f);
			SVector rightUpper = new SVector(0.5f+(maxShield-shield)/maxShield*0.5f,1.0f);
			// Shield
			list.add(new SRenderObject("res/entity/ShieldBar.png", pos.add(0,62*getBody().getScale()), -90.0f,1.0f,1.0f, new Color(255,255,255,0), leftBottom, rightUpper, 2.1f));
			//Life
			SVector leftBottomLife = new SVector((maxLife-life)/maxLife*0.5f,0.0f);
			SVector rightUpperLife = new SVector(0.5f+(maxLife-life)/maxLife*0.5f,1.0f);
			list.add(new SRenderObject("res/entity/HealthBar.png", pos.add(0,55*getBody().getScale()), -90.0f,1.0f,1.0f, new Color(255,255,255,0), leftBottomLife, rightUpperLife, 2.1f));
		} else if (playerGameState.equals(PlayerGameState.Respawning)){
			list.add(new SRenderObject(body.getTexture(), pos, lookDir.getAngle(), body.getCurrentDrawScale(), body.getTransparency(), body.getColor(), 2.0f));
		}
		
		return list;
	}
	public void tryToFire(){
		if (!activeWeapon.tryIt())
			activeWeapon.coolIt();
	}
	
	public void respawn(){
		this.life = maxLife;
		this.shield = maxShield;
		Random random = new Random();
		this.pos = new SVector(random.nextFloat()*7000-3500,random.nextFloat()*7000-3500);
		this.moveDir = new SVector();
		this.acclDir = new SVector();
		this.lookDir = new SVector(1,0);
	}
	
	public boolean gotHit(float damage){
		if (undamagable)
			return false;
		//if we got hit then do not allow shield recharge
		this.shieldRechargeDelay = this.maxShieldRechargeDelay;
		if (damage > this.shield){
			// if it breaks our shield
			damage -= this.shield;
			this.shield = 0.0f;
			this.life -= damage;
			if (this.life <= 0){
				this.life = 0;
				SM explosionMessage = SMPatterns.getAnimationObjectCreateMessage(getPos(), (byte)60);
				SMain.getCommunicationHandler().SendMessage(explosionMessage);
				setPlayerGameState(PlayerGameState.Dead);
				this.player.addDeath(1);
				return true;
			}
			else {
				return false;
			}
		}
		else{
			this.shield -= damage;
		}
		return false;
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

	public float getShield() {
		return shield;
	}

	public void setShield(float shield) {
		this.shield = shield;
	}

	public float getMaxShield() {
		return maxShield;
	}

	public float getShieldRechargeRate() {
		return shieldRechargeRate;
	}

	public float getShieldRechargeDelay() {
		return shieldRechargeDelay;
	}
	
	public void rechargeShield(){
		if (this.getSpeed() < 1.0f)
			this.shieldRechargeDelay -= 3;
		else{
			this.shieldRechargeDelay--;
		}
		if (this.shieldRechargeDelay <= 0){
			this.shieldRechargeDelay = 0;
			if (this.getSpeed() < 1.0f)
				this.shield += this.shieldRechargeRate * 2;
			else{
				this.shield += this.shieldRechargeRate;
			}
			
			if (this.shield > this.maxShield){
				this.shield = this.maxShield;
			}
		}
	}

	public PlayerGameState getPlayerGameState() {
		return playerGameState;
	}

	public void setPlayerGameState(PlayerGameState playerGameState) {
		this.playerGameState = playerGameState;
		if (SMain.IsServer()){
			SM message = SMPatterns.getEntityUpdateStateMessage(this);
	    	SMain.getCommunicationHandler().SendMessage(message);
		}
	}
	public byte getPlayerGameStateId(){
		if (playerGameState.equals(PlayerGameState.Alive))
			return 1;
		else if (playerGameState.equals(PlayerGameState.Respawning))
			return 2;
		else if (playerGameState.equals(PlayerGameState.Dead))
			return 3;
		return 0;
	}
	public void setPlayerGameState(byte state) {
		if (state == 1)
			playerGameState = PlayerGameState.Alive;
		if (state == 2)
			playerGameState = PlayerGameState.Respawning;
		if (state == 3)
			playerGameState = PlayerGameState.Dead;
	}
	
	public List<SWeapon> getWeapons(){
		return weapons;
	}
	public SWeapon getActiveWeapon(){
		return activeWeapon;
	}

	public boolean isUndamagable() {
		return undamagable;
	}

	public void setUndamagable(boolean undamagable) {
		this.undamagable = undamagable;
	}
}
