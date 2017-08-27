package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUp;

public class SPowerUpControlClient extends SControlClient<SPowerUp> {
	protected int currentLifeTime = 0;
	protected int maxLifeTime = 100;
	protected float growingRate = 0.001f;
	
	protected int currentTime = 0;
	protected int duration;
	
	
	private float defaultScale;
	private float currentScale;
	private float growing = 0;
	
	public SPowerUpControlClient(SPowerUp mobile){
		super(mobile);
		defaultScale = Owner.getBody().getScale();
		currentScale = defaultScale;
	}

	@Override
	protected void Think() {
		if (Owner.getObjectState().equals(ObjectState.Active)){
			if (duration > 0){
				currentTime++;
				if (currentTime >= duration * 1.5f){	//automatic delete in case server does not send it
					Owner.setObjectState(ObjectState.WaitingDelete);
					return;
				} else if (currentTime > duration * 0.8f && currentTime < duration){
					currentScale = defaultScale	- (((float)currentTime)/duration - 0.8f) * (defaultScale - (maxLifeTime+1)*growingRate)/0.2f;
				} else if (currentTime > duration) {
					currentScale = 0.01f;
					growing = 0.0f;
					growingRate = 0.0f;
				}
			}
			
			if (currentLifeTime < maxLifeTime){
				currentLifeTime++;
				growing = growing + growingRate;
				Owner.getBody().setScale(currentScale+growing);
			}
			else{
				growingRate = -1*growingRate;
				currentLifeTime = 0;
			}
		}
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
