package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUp;

public class SPowerUpControlClient extends SControlClient<SPowerUp> {
	protected int currentLifeTime = 0;
	protected int maxLifeTime = 100;
	protected float growing = 0.001f;
	
	public SPowerUpControlClient(SPowerUp mobile){
		super(mobile);
	}

	@Override
	protected void Think() {
		if (Owner.getObjectState().equals(ObjectState.Active)){
			if (currentLifeTime < maxLifeTime){
				currentLifeTime++;
				Owner.getBody().setScale(Owner.getBody().getScale()+growing);
			}
			else{
				growing = -1*growing;
				currentLifeTime = 0;
			}
		}
	}
}
