package GameEngine.ObjectEngine.DebrisEngine;

import java.util.Random;

import GameEngine.Specifications;
import GameEngine.ControlEngine.SMineControlClient;
import GameEngine.ControlEngine.SMineControlServer;
import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public class SMine extends SDebris {
	public SMine(SVector pos, SVector moveDir, float scale){
		super();
		this.type = SDebrisFactory.Mine;
		
		this.setPos(new SVector(pos));
		this.moveDir = new SVector(moveDir);
		Random random = new Random();
		this.lookDir = new SVector(1, 0).rotate(random.nextInt(360));
		this.getBody().setTexture("res/entity/CrossHair.png");
		this.getBody().setScale(scale);
		this.getBody().setHitbox(new SHitboxSpherical(this, 48));
		this.getBody().setMass(scale*scale);
		this.setMaxSpeed(Specifications.asteroidMaxSpeed);
		if (SMain.IsServer()){
			this.setController(new SMineControlServer(this));
		}else{
			this.setController(new SMineControlClient(this));
		}
	}
	
	public void addForce(float force){
		//The force and moment depends on the scale of the asteroid
	}
}
