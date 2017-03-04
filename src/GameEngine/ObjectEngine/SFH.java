package GameEngine.ObjectEngine;

import GameEngine.EntityEngine.SEntityFactory;
import GameEngine.ObjectEngine.DebrisEngine.SDebrisFactory;
import GameEngine.ObjectEngine.EffectEngine.SEffectFactory;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUpFactory;
import GameEngine.PlayerEngine.SPlayerFactory;
import GameEngine.WeaponEngine.SBulletFactory;

public class SFH {
	// Factory Handler
	public static SPowerUpFactory PowerUps;
	public static SEffectFactory Effects;
	public static SDebrisFactory Debris;
	public static SEntityFactory Entities;
	public static SPlayerFactory Players;
	public static SBulletFactory Bullets;
	
	public static void initFactories(){
		PowerUps = new SPowerUpFactory();
		Effects = new SEffectFactory();
		Debris = new SDebrisFactory();
		Entities = new SEntityFactory();
		Players = new SPlayerFactory();
		Bullets = new SBulletFactory();
	}
	
	public static void removeObjectFromList(byte FactoryType, int id){
		if (FactoryType == SFH.Bullets.getFactoryType()){
			SFH.Bullets.removeObjectFromList(id);
		}else if (FactoryType == SFH.PowerUps.getFactoryType()){
			SFH.PowerUps.removeObjectFromList(id);
		} else if (FactoryType == SFH.Debris.getFactoryType()){
			SFH.Debris.removeObjectFromList(id);
		} else if (FactoryType == SFH.Effects.getFactoryType()){
			SFH.Effects.removeObjectFromList(id);
		} else if (FactoryType == SFH.Entities.getFactoryType()){
			SFH.Entities.removeObjectFromList(id);
		} else if (FactoryType == SFH.Players.getFactoryType()){
			SFH.Players.removeObjectFromList(id);
		}
	}
}
