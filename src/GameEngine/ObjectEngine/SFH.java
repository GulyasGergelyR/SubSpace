package GameEngine.ObjectEngine;

import GameEngine.EntityEngine.SEntityFactory;
import GameEngine.ObjectEngine.DebrisEngine.SDebrisFactory;
import GameEngine.ObjectEngine.EffectEngine.SEffectFactory;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUpFactory;

public class SFH {
	// Factory Handler
	public static SPowerUpFactory PowerUps;
	public static SEffectFactory Effects;
	public static SDebrisFactory Debris;
	public static SEntityFactory Entities;
	
	public static void initFactories(){
		PowerUps = new SPowerUpFactory();
		Effects = new SEffectFactory();
		Debris = new SDebrisFactory();
		Entities = new SEntityFactory();
	}
}
