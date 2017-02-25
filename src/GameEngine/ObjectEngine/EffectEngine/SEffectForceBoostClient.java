package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;

public class SEffectForceBoostClient extends SEffectClient{
	float defaultMass = 0;
	
	public SEffectForceBoostClient(SMobile Owner) {
		super(Owner);
		duration = 360;
		this.type = SEffectFactory.EffectForceBoost;
	}
	
	@Override
	protected void applyToOwner() {
		((SEntity)Owner).getActiveWeapon().getBaseBullet().getBody().setScale(0.5f);
	}

	@Override
	protected boolean restore() {
		((SEntity)Owner).getActiveWeapon().getBaseBullet().getBody().setScale(0.25f);
		return false;
	}
}
