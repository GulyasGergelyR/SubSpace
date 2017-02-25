package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.BaseEngine.SMobile;

public class SEffectBurstClient extends SEffectClient{
	public SEffectBurstClient(SMobile Owner) {
		super(Owner);
		duration = 720;
		this.type = SEffectFactory.EffectBurst;
	}
}
