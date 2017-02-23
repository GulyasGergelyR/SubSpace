package GameEngine.ObjectEngine.EffectEngine;

import java.util.LinkedList;
import java.util.ListIterator;

import GameEngine.ObjectEngine.EffectEngine.SEffect.EffectState;


public class SEffectFactory {
	protected static LinkedList<SEffect> effects;
	public static final int EffectBurst = 1;
	public static final int EffectForceBoost = 2;
	
	
	public static void init(){
		effects = new LinkedList<SEffect>();
	}
	
	public static void addEffect(SEffect effect){
		effects.add(effect);
	}
	
	public static void UpdateObjects(){
		if(!effects.isEmpty()){
			ListIterator<SEffect> iter = effects.listIterator();
			while(iter.hasNext()){
				SEffect effect = iter.next();
			    if(effect.getEffectState().equals(EffectState.Finished)){
			        iter.remove();
			    }else {
			    	effect.update();
			    	if(effect.getEffectState().equals(EffectState.Finished)){
				        iter.remove();
				    }
			    }
			}
		}
	}
}
