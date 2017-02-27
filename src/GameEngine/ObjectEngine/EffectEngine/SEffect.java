package GameEngine.ObjectEngine.EffectEngine;

import java.util.ListIterator;

import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SUpdatable;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SEffect extends SUpdatable{
	protected SMobile Owner;
	protected int currentTime = 0;
	protected int duration = 100;
	byte type;
	
	public enum EffectState{
		Active, Applied, Finished
	}
	protected EffectState effectState = EffectState.Active;
	
	public SEffect(SMobile Owner){
		super();
		this.Owner = Owner;
		if (canBeApplied()){
			if (!foundPrevious()){
				applyToOwner();
				Owner.addEffect(this);
				setEffectState(EffectState.Active);
			}
		} else{
			setEffectState(EffectState.Finished);
		}
	}
	
	public byte getType(){
		return type;
	}
	
	protected boolean canBeApplied(){
		//default is that we could apply the effect
		return true;
	}
	
	protected boolean foundPrevious(){
		ListIterator<SEffect> iter = Owner.getAppliedEffects().listIterator();
		while(iter.hasNext()){
			SEffect effect = iter.next();
			if (effect.getClass().equals(this.getClass())){
				effect.receiveParameters(this);
				setEffectState(EffectState.Applied);
		        return true;
		    }
		}
		return false;
	}
	
	protected void applyToOwner(){
		
	}
	
	@Override
	public void update(){
		currentTime++;
		if (currentTime >= duration){
			if (!restore()){
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
	
	public void remove(){
		restore();
		setEffectState(EffectState.Finished);
	}
	
	protected void receiveParameters(SEffect effect){
		currentTime = 0;
		SM message = SMPatterns.getObjectUpdateMessage(this);
		SMain.getCommunicationHandler().SendMessage(message);
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
	
	@Override
	public boolean isActive(){
		return this.effectState.equals(EffectState.Active);
	}
	
	public boolean isApplied(){
		return this.effectState.equals(EffectState.Applied);
	}
	
	public SMobile getOwner() {
		return Owner;
	}

	@Override
	public void kill() {
		this.Owner.removeEffect(this);
		SM message = SMPatterns.getObjectDeleteMessage(this);
		SMain.getCommunicationHandler().SendMessage(message);
	}
	
	public int getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public int getDuration() {
		return duration;
	}
	
}
