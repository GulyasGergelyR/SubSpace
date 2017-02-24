package GameEngine.ObjectEngine.PowerUpEngine;

import GameEngine.ControlEngine.SPowerUpControlServer;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public class SPowerUpHeal extends SPowerUp{
	protected static int currentNumberOfPowerUps = 0;
	protected static int maxNumberOfPowerUps = 3;
	
	public SPowerUpHeal(SVector pos){
		super(pos);
		this.type = SPowerUpFactory.PowerUpHeal;
		this.getBody().setTexture("res/object/powerup/powerupheal.png");
		this.setLookDir(new SVector(0, -1));
		if (SMain.IsServer()){
			((SPowerUpControlServer)this.getController()).setDuration(1500);
		}
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
