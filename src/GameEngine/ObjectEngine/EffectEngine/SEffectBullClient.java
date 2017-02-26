package GameEngine.ObjectEngine.EffectEngine;

import org.newdawn.slick.Color;

import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;

public class SEffectBullClient extends SEffectClient {
	public SEffectBullClient(SMobile Owner){
		super(Owner);
		duration = Specifications.effectBullDuration;
		this.type = SEffectFactory.EffectBull;
	}
	
	@Override
	protected void applyToOwner() {
		((SEntity)Owner).getBody().setColor(new Color(1.0f, 0.0f, 0.0f));
	}
	
	@Override
	protected boolean restore() {
		((SEntity)Owner).getBody().setColor(new Color(1.0f, 1.0f, 1.0f));
		return false;
	}
}
