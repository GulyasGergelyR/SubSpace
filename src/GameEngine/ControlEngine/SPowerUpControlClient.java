package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUp;

public class SPowerUpControlClient extends SControlClient<SPowerUp> {
	protected int currentLifeTime = 0;
	protected int maxLifeTime = 100;
	protected float growing = 0.001f;
	
	protected int currentTime = 0;
	protected int duration;
	
	public SPowerUpControlClient(SPowerUp mobile){
		super(mobile);
	}

	@Override
	protected void Think() {
		if (Owner.getObjectState().equals(ObjectState.Active)){
			if (duration > 0){
				currentTime++;
				if (currentTime >= duration * 1.5f){	//automatic delete in case server does not send it
					Owner.setObjectState(ObjectState.WaitingDelete);
					return;
				}
			}
			
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
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
