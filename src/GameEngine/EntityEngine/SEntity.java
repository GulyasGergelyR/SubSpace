package GameEngine.EntityEngine;

import java.util.List;

import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.GeomEngine.SVector;
import RenderingEngine.SRenderObject;

public class SEntity extends GameEngine.BaseEngine.SMobile{
	
	protected float life;
	// TODO Create SWeapon
	// protected List<SWeapon> weapons;
	
	public SEntity(){
		super();
		this.pos = new SVector(250.0f,250.0f);
		this.getBody().setTexture("res/entity/spaceshipv1.png");
		this.getBody().setScale(0.05f);
		this.getBody().setHitbox(new SHitboxSpherical(this, 10f));
		this.setController(new SHumanControlLocal(this));
	}

	@Override
	public List<SRenderObject> getDrawables() {
		//TODO Add movement and life specific drawings
		List<SRenderObject> list = super.getDrawables();
		//list.add(new SRenderObject("res/dot.png", pos, 0.0f, 0.5f, 1.0f));
		return list;
	}	
}
