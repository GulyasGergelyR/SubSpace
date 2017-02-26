package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;

public class SEffectBurstClient extends SEffectClient{
	public SEffectBurstClient(SMobile Owner) {
		super(Owner);
		duration = Specifications.effectBurstDuration;
		this.type = SEffectFactory.EffectBurst;
	}
}
