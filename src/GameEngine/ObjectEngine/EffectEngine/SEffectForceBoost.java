package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;

public class SEffectForceBoost extends SEffect{
	float defaultMass = 0;
	
	public SEffectForceBoost(SMobile Owner) {
		super(Owner);
		duration = Specifications.effectForceBoostDuration;
		this.type = SEffectFactory.EffectForceBoost;
	}

	@Override
	protected void applyToOwner() {
		((SEntity)Owner).getActiveWeapon().getBaseBullet().getBody().setMass(2);
		((SEntity)Owner).getActiveWeapon().getBaseBullet().setDamage(20);
	}

	@Override
	protected boolean restore() {
		((SEntity)Owner).getActiveWeapon().getBaseBullet().getBody().setMass(0.03f);
		((SEntity)Owner).getActiveWeapon().getBaseBullet().setDamage(Specifications.bulletDamage);
		return false;
	}
}
