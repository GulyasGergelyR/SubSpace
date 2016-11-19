package GameEngine.EntityEngine;

import java.util.List;

import GameEngine.SPlayer;
import GameEngine.SPlayer.PlayerState;
import GameEngine.ControlEngine.SHumanControlServer;
import GameEngine.ControlEngine.SHumanControlClient;
import GameEngine.GeomEngine.SVector;
import Main.SMain;
import RenderingEngine.SRenderObject;
import WebEngine.ComEngine.SCommunicationHandler.UDPRole;

public class SEntity extends GameEngine.BaseEngine.SMobile{
	public enum EntityState{
		//TODO add this to SObject
		Active, Ghost, Invisible, OnDeathRaw
	}
	protected EntityState entityState = EntityState.Active;
	protected SPlayer player;
	
	
	protected float life;
	// TODO Create SWeapon
	// protected List<SWeapon> weapons;
	@Deprecated
	public SEntity(){
		super();
		this.pos = new SVector(250.0f,250.0f);
		this.texture = "res/entity/spaceshipv1.png";
		this.scale = 0.05f;
		this.setController(new SHumanControlClient(this));
	}
	
	public SEntity(SPlayer player){
		super();
		this.pos = new SVector(250.0f,250.0f);
		this.texture = "res/entity/spaceshipv1.png";
		this.scale = 0.05f;
		this.player = player;
		player.setEntity(this);
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

	public EntityState getState(){
		return entityState;
	}
	
}
