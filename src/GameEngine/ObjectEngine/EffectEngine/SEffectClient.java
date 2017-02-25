package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.BaseEngine.SMobile;

public class SEffectClient extends SEffect{
	public SEffectClient(SMobile Owner){
		super(Owner);
		duration = 720;
		this.type = SEffectFactory.EffectBurst;
	}

	@Override
	public void update(){
			if (currentTime < duration){
				currentTime++;
			}
	}
	
	@Override
	protected boolean foundPrevious() {
		return false;
	}
	
	@Override
	protected void receiveParameters(SEffect effect) {
	}
	
	@Override
	public void kill() {
		restore();
		this.Owner.removeEffect(this);
	}
	
}
