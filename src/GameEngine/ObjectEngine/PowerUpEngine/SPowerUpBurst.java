package GameEngine.ObjectEngine.PowerUpEngine;

import GameEngine.ControlEngine.SPowerUpControlServer;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.EffectEngine.SEffectBurst;
import GameEngine.ObjectEngine.EffectEngine.SEffectFactory;
import Main.SMain;

public class SPowerUpBurst extends SPowerUp {
	protected static int currentNumberOfPowerUps = 0;
	protected static int maxNumberOfPowerUps = 3;
	
	public SPowerUpBurst(SVector pos) {
		super(pos);
		this.type = SPowerUpFactory.PowerUpBurst;
		this.getBody().setTexture("res/object/powerup/powerupburst.png");
		if (SMain.IsServer()){
			((SPowerUpControlServer)this.getController()).setDuration(0);
		}
	}

	@Override
	public boolean applyToEntity(SEntity entity) {
		SEffectBurst effectBurst = new SEffectBurst(entity);
		SEffectFactory.addEffect(effectBurst);
		return true;
	}
}
