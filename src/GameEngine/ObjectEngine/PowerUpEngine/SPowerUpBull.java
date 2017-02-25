package GameEngine.ObjectEngine.PowerUpEngine;

import GameEngine.ControlEngine.SPowerUpControlServer;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.EffectEngine.SEffect.EffectState;
import GameEngine.ObjectEngine.EffectEngine.SEffectBull;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SPowerUpBull extends SPowerUp {
	protected static int currentNumberOfPowerUps = 0;
	protected static int maxNumberOfPowerUps = 2;
	
	public SPowerUpBull(SVector pos){
		super(pos);
		this.type = SPowerUpFactory.PowerUpBull;
		this.getBody().setTexture("res/object/powerup/powerupbull.png");
		this.setLookDir(new SVector(0, -1));
		if (SMain.IsServer()){
			((SPowerUpControlServer)this.getController()).setDuration(900);
		}
	}
	
	@Override
	public boolean applyToEntity(SEntity entity) {
		SEffectBull effectBull = new SEffectBull(entity);
		if (effectBull.isActive()){
			SFH.Effects.createNewEffectAtServer(effectBull);
			return true;
		}  else if (effectBull.isApplied()){
			return true;
		} else{
			return false;
		}
	}
}
