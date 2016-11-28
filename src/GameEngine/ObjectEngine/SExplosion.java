package GameEngine.ObjectEngine;

import GameEngine.BaseEngine.SObject;
import GameEngine.GeomEngine.SVector;

public class SExplosion extends SObject{
	
	protected int currentLife = 0;
	protected int maxLife = 6;
	protected float growing = 0.05f;
	
	public SExplosion(SVector pos){
		super();
		this.pos = new SVector(pos);
		this.getBody().setTexture("res/object/explosion/explosion.png");
		this.getBody().setScale(0.0f);
	}

	@Override
	public void update() {
		this.getBody().setScale(this.getBody().getScale()+growing);
		currentLife++;
		if(currentLife>=maxLife){
			this.setObjectState(ObjectState.WaitingDelete);
		}
	}
}
