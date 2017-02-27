package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;

public class SEffectBull extends SEffect{
	public SEffectBull(SMobile Owner){
		super(Owner);
		duration = Specifications.effectBullDuration;
		this.type = SEffectFactory.EffectBull;
	}
	
	@Override
	protected void applyToOwner() {
		((SEntity)Owner).setUndamagableByCollision(true);
		((SEntity)Owner).getBody().setMass(100.f);
	}
	
	@Override
	protected boolean restore() {
		((SEntity)Owner).setUndamagableByCollision(false);
		((SEntity)Owner).getBody().setMass(Specifications.entityMass);
		return false;
	}
}
