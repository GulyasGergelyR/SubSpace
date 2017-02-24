package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;

public class SEffectBull extends SEffect{
	public SEffectBull(SMobile Owner){
		super(Owner);
		duration = 900;
	}
	
	@Override
	protected void applyToOwner() {
		((SEntity)Owner).setUndamagable(true);
		((SEntity)Owner).getBody().setMass(100.f);
	}
	
	@Override
	protected boolean restore() {
		((SEntity)Owner).setUndamagable(false);
		((SEntity)Owner).getBody().setMass(0.0f);
		return false;
	}

	@Override
	protected void affect() {
		applyToOwner();
	}
}
