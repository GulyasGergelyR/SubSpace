package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;

public class SEffectForceBoost extends SEffect{
	float defaultMass = 0;
	
	public SEffectForceBoost(SMobile Owner) {
		super(Owner);
		duration = 360;
	}

	@Override
	protected void applyToOwner() {
		((SEntity)Owner).getActiveWeapon().getBaseBullet().getBody().setMass(2);
	}

	@Override
	protected void restore() {
		((SEntity)Owner).getActiveWeapon().getBaseBullet().getBody().setMass(0.03f);
	}
	
	@Override
	protected void affect() {
		applyToOwner();
	}
	
}
