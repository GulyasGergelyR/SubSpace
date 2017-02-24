package GameEngine.ObjectEngine.EffectEngine;

import java.util.LinkedList;

import GameEngine.ObjectEngine.SFactory;


public class SEffectFactory extends SFactory<SEffect> {
	protected LinkedList<SEffect> effects;
	public final int EffectBurst = 1;
	public final int EffectForceBoost = 2;
	public final int EffectBull = 3;
	
	public SEffectFactory(){
		super();
		this.FactoryName = "Effect factory";
	}
}
