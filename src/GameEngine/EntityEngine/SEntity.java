package GameEngine.EntityEngine;

import java.util.ArrayList;
import java.util.List;

import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.SPlayer;
import GameEngine.SPlayer.PlayerState;
import GameEngine.ControlEngine.SHumanControlClient;
import GameEngine.ControlEngine.SHumanControlServer;
import GameEngine.GeomEngine.SVector;
import GameEngine.WeaponEngine.SWeapon;
import Main.SMain;
import RenderingEngine.SRenderObject;
import WebEngine.ComEngine.SCommunicationHandler.UDPRole;

public class SEntity extends GameEngine.BaseEngine.SMobile{
	protected SPlayer player;
	protected float life;
	protected List<SWeapon> weapons;
	protected SWeapon activeWeapon;
	
	@Deprecated
	public SEntity(){
		super();
		this.pos = new SVector(250.0f,250.0f);
		this.getBody().setTexture("res/entity/spaceshipv1.png");
		this.getBody().setScale(0.05f);
		this.getBody().setHitbox(new SHitboxSpherical(this, 10f));
		this.setController(new SHumanControlClient(this));
	}
	
	public SEntity(SPlayer player){
		super();
		this.pos = new SVector(250.0f,250.0f);
		this.getBody().setTexture("res/entity/spaceshipv1.png");
		this.getBody().setScale(0.05f);
		this.getBody().setHitbox(new SHitboxSpherical(this, 10f));
		this.player = player;
		player.setEntity(this);
		// Add weapons
		weapons = new ArrayList<SWeapon>();
		SWeapon weapon = new SWeapon(this);
		activeWeapon = weapon;
		if (player.getPlayerState().equals(PlayerState.local)){
			System.out.println("Created local player at: "+SMain.getCommunicationHandler().getUDPRole());
			this.setController(new SHumanControlClient(this));
		}else if(SMain.getCommunicationHandler().getUDPRole().equals(UDPRole.Server)){
			System.out.println("Created lan player at server");
			this.setController(new SHumanControlServer(this));
		} else {
			System.out.println("Created lan player at client");
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
}
