package GameEngine.ObjectEngine.PowerUpEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.ControlEngine.SPowerUpControlServer;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public class SPowerUp extends SMobile{
	protected byte type = 0;
	
	// For the pulsing 
	protected int currentLifeTime = 0;
	protected int maxLifeTime = 100;
	
	protected float growing = 0.001f;
	
	public SPowerUp(SVector pos){
		super();
		this.pos = new SVector(pos);
		this.getBody().setTexture("res/object/powerup/powerupring.png");
		this.getBody().setScale(0.15f);
		this.getBody().setHitbox(new SHitboxSpherical(this, 120));
		if (SMain.IsServer()){
			this.setController(new SPowerUpControlServer(this));
		}
	}
	public boolean applyToEntity(SEntity entity){return false;}
	public byte getType(){
		return type;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
		if (getObjectState().equals(ObjectState.Active)){
			if (currentLifeTime < maxLifeTime){
				currentLifeTime++;
				getBody().setScale(getBody().getScale()+growing);
			}
			else{
				growing = -1*growing;
				currentLifeTime = 0;
			}
		}
	}
}
