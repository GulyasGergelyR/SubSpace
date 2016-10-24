package GameEngine.EntityEngine;

import java.util.List;

import GameEngine.SResLoader;
import GameEngine.GeomEngine.SVector;
import RenderingEngine.SRenderObject;

public class SEntity extends GameEngine.BaseEngine.SMobile{
	public enum EntityState{
		//TODO add this to SObject
		Active, Ghost, Invisible, OnDeathRaw
	}
	protected EntityState entityState = EntityState.Active;
	
	public SEntity(){
		super();
		this.pos = new SVector(250.0f,250.0f);
		this.texture = "res/entity/spaceshipv1.png";
		this.scale = 0.1f;
		this.setController(new SHumanControl(this));
	}

	@Override
	public List<SRenderObject> Draw() {
		List<SRenderObject> list = super.Draw();
		list.add(new SRenderObject("res/dot.png", pos, 0.0f, 0.5f, 1.0f));
		return list;
	}

	public EntityState getState(){
		return entityState;
	}
	
}
