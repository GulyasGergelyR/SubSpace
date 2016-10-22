package GameEngine.EntityEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GameEngine.BaseEngine.SMobile;
import GameEngine.GeomEngine.SVector;

public class SHumanControl extends SControl{
	
	public SHumanControl(SMobile mobile){
		super(mobile);
	}

	@Override
	public void Think() {
		SVector acclDir = new SVector();
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			acclDir = acclDir.add(-1.0f, 0.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			acclDir = acclDir.add(1.0f, 0.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			acclDir = acclDir.add(0.0f, 1.0f);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			acclDir = acclDir.add(0.0f, -1.0f);
		}
		
		if(acclDir.l()==0){
			Owner.setAcclDir(Owner.getMoveDir().setLength(-Owner.getMaxAcceleration()/2.0f));
		}
		else{
			Owner.setAcclDir(acclDir.setLength(Owner.getMaxAcceleration()));
		}
		Owner.Move();
		
		int M_x = Mouse.getX();
		int M_y = Mouse.getY();
		Owner.setLookDir(new SVector(M_x,M_y).sub(Owner.getPos()));
	}
}
