package GameEngine.ControlEngine;

import java.util.Random;

import GameEngine.ObjectEngine.DebrisEngine.SDebris;
import Main.SMain;

public class SMineControlClient extends SControlServer<SDebris>{
	float rotateDir;
	float rotSpeed;
	
	public SMineControlClient(SDebris mobile){
		super(mobile);
		Random random = new Random();
		this.rotateDir = random.nextInt(2) * 2 - 1;
		this.rotSpeed = random.nextFloat() * 5;
	}
	@Override
	protected void Think() {
		Owner.setLookDir(Owner.getLookDir().rotate(rotateDir*rotSpeed*SMain.getDeltaRatio()));
	}
}
