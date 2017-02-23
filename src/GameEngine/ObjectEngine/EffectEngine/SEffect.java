package GameEngine.ObjectEngine.EffectEngine;

import GameEngine.BaseEngine.SMobile;

public class SEffect {
	protected SMobile Owner;
	protected int currentTime = 0;
	protected int duration = 100;
	
	public enum EffectState{
		Active, Finished
	}
	protected EffectState effectState;
	
	public SEffect(SMobile Owner){
		this.Owner = Owner;
		applyToOwner();
		setEffectState(EffectState.Active);
	}
	
	protected void applyToOwner(){
		
	}
	
	protected void update(){
		currentTime++;
		if (currentTime >= duration){
			end();
			restore();
			setEffectState(EffectState.Finished);
		} else {
			affect();
		}
	}
	
	protected void affect(){
		
	}
	
	protected void restore(){
		
	}
	
	protected void end(){
		
	}

	public EffectState getEffectState() {
		return effectState;
	}

	public void setEffectState(EffectState effectState) {
		this.effectState = effectState;
	}
	
	
}
