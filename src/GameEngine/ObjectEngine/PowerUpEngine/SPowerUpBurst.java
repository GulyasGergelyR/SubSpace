package GameEngine.ObjectEngine.PowerUpEngine;

import GameEngine.Specifications;
import GameEngine.ControlEngine.SPowerUpControlServer;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.EffectEngine.SEffectBurst;
import Main.SMain;

public class SPowerUpBurst extends SPowerUp {
	protected static int currentNumberOfPowerUps = 0;
	protected static int maxNumberOfPowerUps = Specifications.maxNumberOfPowerUpBurst;
	
	public SPowerUpBurst(SVector pos) {
		super(pos, Specifications.powerupBurstDuration);
		this.type = SPowerUpFactory.PowerUpBurst;
		this.getBody().setTexture("res/object/powerup/powerupburst.png");
		this.setLookDir(new SVector(0, -1));
	}

	@Override
	public boolean applyToEntity(SEntity entity) {
		SEffectBurst effectBurst = new SEffectBurst(entity);
		if (effectBurst.isActive()){
			SFH.Effects.createNewEffectAtServer(effectBurst);
			return true;
		} else if (effectBurst.isApplied()){
			return true;
		} else{
			return false;
		}
	}
}
