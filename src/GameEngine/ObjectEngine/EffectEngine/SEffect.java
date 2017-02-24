package GameEngine.ObjectEngine.EffectEngine;

import java.util.ListIterator;

import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SUpdatable;

public class SEffect extends SUpdatable{
	protected SMobile Owner;
	protected int currentTime = 0;
	protected int duration = 100;
	
	public enum EffectState{
		Active, Finished
	}
	protected EffectState effectState = EffectState.Active;
	
	public SEffect(SMobile Owner){
		super();
		this.Owner = Owner;
		if (canBeApplied()){
			checkPrevious();
			applyToOwner();
			Owner.addEffect(this);
			setEffectState(EffectState.Active);
		} else{
			setEffectState(EffectState.Finished);
		}
	}
	
	protected boolean canBeApplied(){
		//default is that we could apply the effect
		return true;
	}
	
	private void checkPrevious(){
		ListIterator<SEffect> iter = Owner.getAppliedEffects().listIterator();
		while(iter.hasNext()){
			SEffect effect = iter.next();
			if (effect.getClass().equals(this.getClass())){
				this.receiveParameters(effect);
		        iter.remove();
		        break;
		    }
		}
	}
	
	protected void applyToOwner(){
		
	}
	
	@Override
	public void update(){
		currentTime++;
		if (currentTime >= duration){
			if (!restore()){
				Owner.removeEffect(this);
				setEffectState(EffectState.Finished);
			}
		} else {
			affect();
		}
	}
	
	protected void affect(){
		
	}
	
	protected boolean restore(){
		//by default we remove the effect
		return false;
	}
	
	protected void receiveParameters(SEffect effect){
		
	}

	public EffectState getEffectState() {
		return effectState;
	}

	public void setEffectState(EffectState effectState) {
		this.effectState = effectState;
	}

	@Override
	public boolean shouldBeDeleted() {
		return this.effectState.equals(EffectState.Finished);
	}
	
	
	
}
