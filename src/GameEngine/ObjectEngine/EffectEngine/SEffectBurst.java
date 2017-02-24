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
		((SEntity)Owner).getActiveWeapon().setCoolTime(3);
	}
	
	@Override
	protected boolean restore() {
		((SEntity)Owner).getActiveWeapon().setBurstMode(false);
		((SEntity)Owner).getActiveWeapon().setCoolTime(7);
		return false;
	}	
}
