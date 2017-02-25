package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.BaseEngine.SMobile;

public class SEffectForceBoostClient extends SEffectClient{
	float defaultMass = 0;
	
	public SEffectForceBoostClient(SMobile Owner) {
		super(Owner);
		duration = 360;
		this.type = SEffectFactory.EffectForceBoost;
	}
}
