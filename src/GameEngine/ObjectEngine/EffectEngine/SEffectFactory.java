package GameEngine.ObjectEngine.EffectEngine;

import java.util.LinkedList;

import GameEngine.SId;
import GameEngine.BaseEngine.SMobile;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.SFactory;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;


public class SEffectFactory extends SFactory<SEffect> {
	protected LinkedList<SEffect> effects;
	public static final byte EffectBurst = 1;
	public static final byte EffectForceBoost = 2;
	public static final byte EffectBull = 3;
	public static final byte EffectBlackHole = 4;
	
	public SEffectFactory(){
		super("Effect factory", (byte) 70);
	}
	
	public void createNewEffectAtClient(int id, int ownerId,int duration, byte effectType){
		SEffect effect = null;
		SMobile mobile = SFH.Entities.getObjectById(ownerId);
		if (effectType == EffectBurst){
			effect = new SEffectBurstClient(mobile);
		} else if (effectType == EffectForceBoost){
			effect = new SEffectForceBoostClient(mobile);
		} else if (effectType == EffectBull){
			effect = new SEffectBullClient(mobile);
		} 
		effect.setId(new SId(id));
		effect.setDuration(duration);
		addObject(effect);
	}
	
	public void createNewEffectAtServer(SEffect effect){
		addObject(effect);
		SM message = SMPatterns.getObjectCreateMessage(effect);
		SMain.getCommunicationHandler().SendMessage(message);
	}
}
