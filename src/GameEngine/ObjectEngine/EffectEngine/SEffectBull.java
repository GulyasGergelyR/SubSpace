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
		Owner.getBody().setMass(100.f);
		Owner.setMaxSpeed(70);
		Owner.setMaxAcceleration(16);
	}
	
	@Override
	protected boolean restore() {
		((SEntity)Owner).setUndamagableByCollision(false);
		Owner.getBody().setMass(Specifications.entityMass);
		Owner.setMaxSpeed(Specifications.entityMaxSpeed);
		Owner.setMaxAcceleration(Specifications.entityMaxAccl);
		return false;
	}
}
