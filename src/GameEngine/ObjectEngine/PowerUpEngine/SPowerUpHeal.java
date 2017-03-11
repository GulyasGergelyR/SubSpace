package GameEngine.ObjectEngine.PowerUpEngine;

import GameEngine.Specifications;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;

public class SPowerUpHeal extends SPowerUp{
	protected static int currentNumberOfPowerUps = 0;
	protected static int maxNumberOfPowerUps = Specifications.maxNumberOfPowerUpHeal;
	
	public SPowerUpHeal(SVector pos){
		super(pos, Specifications.powerupHealDuration);
		this.type = SPowerUpFactory.PowerUpHeal;
		this.getBody().setTexture("res/object/powerup/powerupheal.png");
		this.setLookDir(new SVector(0, -1));
	}

	@Override
	public boolean applyToEntity(SEntity entity) {
		if (entity.getLife() < entity.getMaxLife()){
			entity.setLife(entity.getLife()+Specifications.powerupHealAmount);
			return true;
			}
		else return false;
	
	}
	
}
