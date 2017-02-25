package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;

public class SEffectForceBoost extends SEffect{
	float defaultMass = 0;
	
	public SEffectForceBoost(SMobile Owner) {
		super(Owner);
		duration = 360;
		this.type = SEffectFactory.EffectForceBoost;
	}

	@Override
	protected void applyToOwner() {
		((SEntity)Owner).getActiveWeapon().getBaseBullet().getBody().setMass(2);
	}

	@Override
	protected boolean restore() {
		((SEntity)Owner).getActiveWeapon().getBaseBullet().getBody().setMass(0.03f);
		return false;
	}
}
