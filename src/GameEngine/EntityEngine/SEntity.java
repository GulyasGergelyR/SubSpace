package GameEngine.EntityEngine;

import java.util.List;

import GameEngine.SPlayer;
import GameEngine.SResLoader;
import GameEngine.GeomEngine.SVector;
import RenderingEngine.SRenderObject;

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
		this.setController(new SHumanControlLocal(this));
	}
	
	public SEntity(SPlayer player){
		super();
		this.pos = new SVector(250.0f,250.0f);
		this.texture = "res/entity/spaceshipv1.png";
		this.scale = 0.05f;
		this.setController(new SHumanControlLocal(this));
	}
		
	@Override
	public List<SRenderObject> getDrawables() {
		//TODO Add movement and life specific drawings
		List<SRenderObject> list = super.getDrawables();
		//list.add(new SRenderObject("res/dot.png", pos, 0.0f, 0.5f, 1.0f));
		return list;
	}

	public EntityState getState(){
		return entityState;
	}
	
}
