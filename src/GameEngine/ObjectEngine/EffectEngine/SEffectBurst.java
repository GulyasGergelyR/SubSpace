package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;

public class SEffectBurst extends SEffect{
	public SEffectBurst(SMobile Owner) {
		super(Owner);
		duration = 720;
	}

	@Override
	protected void applyToOwner() {
		((SEntity)Owner).getActiveWeapon().setBurstMode(true);
	}
	
	@Override
	protected void restore() {
		((SEntity)Owner).getActiveWeapon().setBurstMode(false);
	}

	@Override
	protected void affect() {
		applyToOwner();
	}
	
	
}
