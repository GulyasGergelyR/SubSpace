package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;

public class SEffectBlackHole extends SEffect {
	public SEffectBlackHole(SMobile Owner){
		super(Owner);
		duration = Specifications.effectBlackHoleDuration;
		this.type = SEffectFactory.EffectBlackHole;
	}
}
