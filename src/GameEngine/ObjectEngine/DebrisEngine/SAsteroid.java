package GameEngine.ObjectEngine.DebrisEngine;

import java.util.Random;

import GameEngine.ControlEngine.SAsteroidControlClient;
import GameEngine.ControlEngine.SAsteroidControlServer;
import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public class SAsteroid extends SDebris {
	public SAsteroid(SVector pos, SVector moveDir, float scale){
		super();
		this.type = SDebrisFactory.Asteroid;
		
		this.pos = new SVector(pos);
		this.moveDir = new SVector(moveDir);
		Random random = new Random();
		this.lookDir = new SVector(1, 0).rotate(random.nextInt(360));
		this.getBody().setTexture("res/object/asteroid/asteroidv2.png");
		this.getBody().setScale(scale);
		this.getBody().setHitbox(new SHitboxSpherical(this, 48));
		this.getBody().setMass(scale*scale);
		if (SMain.IsServer()){
			this.setController(new SAsteroidControlServer(this));
		}else{
			this.setController(new SAsteroidControlClient(this));
		}
	}
	
	public void addForce(float force){
		//The force and moment depends on the scale of the asteroid
	}
}
