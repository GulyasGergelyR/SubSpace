package GameEngine.ObjectEngine.PowerUpEngine;

import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;

public class SPowerUpHeal extends SPowerUp{
	public SPowerUpHeal(SVector pos){
		super(pos);
		this.type = SPowerUpFactory.PowerUpHeal;
		this.getBody().setTexture("res/object/powerup/powerupheal.png");
	}

	@Override
	public boolean applyToEntity(SEntity entity) {
		if (entity.getLife() < entity.getMaxLife()){
			entity.setLife(entity.getLife()+50);
			return true;
			}
		else return false;
	
	}
	
}
