package GameEngine.ObjectEngine.PowerUpEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.ControlEngine.SPowerUpControlClient;
import GameEngine.ControlEngine.SPowerUpControlServer;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public class SPowerUp extends SMobile{
	protected byte type = 0;
	protected int duration = 0;
	
	public SPowerUp(SVector pos,int duration){
		super();
		this.duration = duration;
		this.setPos(new SVector(pos));
		this.getBody().setTexture("res/object/powerup/powerupring.png");
		this.getBody().setScale(0.15f);
		this.getBody().setHitbox(new SHitboxSpherical(this, 120));
		if (SMain.IsServer()){
			this.setController(new SPowerUpControlServer(this));
			((SPowerUpControlServer)this.getController()).setDuration(duration);
		}else{
			this.setController(new SPowerUpControlClient(this));
		}
	}
	public boolean applyToEntity(SEntity entity){return false;}
	public byte getType(){
		return type;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
		if (SMain.IsServer()){
			((SPowerUpControlServer)this.getController()).setDuration(duration);
		}else{
			((SPowerUpControlClient)this.getController()).setDuration(duration);	
		}
	}
}
