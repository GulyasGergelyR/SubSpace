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
		if (Owner.getAcclDir().l() > 0.0f){
			Owner.getBody().setTexture("res/object/mine/Mine_red.png");
		} else {
			Owner.getBody().setTexture("res/object/mine/Mine_yellow.png");
		}
	}
}
