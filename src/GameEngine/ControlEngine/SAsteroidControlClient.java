package GameEngine.ControlEngine;

import java.util.Random;

import GameEngine.BaseEngine.SMobile;
import Main.SMain;

public class SAsteroidControlClient extends SControlServer{
	float rotateDir;
	float rotSpeed;
	
	public SAsteroidControlClient(SMobile mobile){
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
