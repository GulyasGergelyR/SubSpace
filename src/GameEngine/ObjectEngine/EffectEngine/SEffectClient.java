package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.BaseEngine.SMobile;
import Main.SMain;

public class SEffectClient extends SEffect{
	protected float hudPosition = -30;
	protected float aimedHudPosition = -30;
	
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
			
			if (hudPosition>aimedHudPosition){
				hudPosition -= 3*SMain.getDeltaRatio();
			} else if (hudPosition < aimedHudPosition){
				hudPosition = aimedHudPosition;
			}
	}
	
	@Override
	protected boolean foundPrevious() {
		return false;
	}
	
	@Override
	protected void receiveParameters(SEffect effect) {
		this.hudPosition = ((SEffectClient) effect).hudPosition;
		this.aimedHudPosition = ((SEffectClient) effect).aimedHudPosition;
	}
	
	@Override
	public void kill() {
		restore();
		this.Owner.removeEffect(this);
	}

	public float getHudPosition() {
		return hudPosition;
	}

	public void setAimedHudPosition(float aimedHudPosition) {
		this.aimedHudPosition = aimedHudPosition;
	}
	
}
