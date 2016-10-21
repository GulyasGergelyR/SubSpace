package GameEngine.EntityEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GameEngine.BaseEngine.SObject;
import GameEngine.GeomEngine.SVector;

public class SHumanControl extends SControl{
	
	public SHumanControl(SObject O){
		super(O);
	}

	@Override
	public void Think() {
		SVector moveDir = new SVector();
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			
		}
	}
	
	/*public void set_look_dir() {
		int M_x = Mouse.getX();
		int M_y = Mouse.getY();
		
		System.out.printf("%f, %f\n",pos.get_x(),pos.get_y());
		vector look_at=new vector(M_x,M_y);
		super.set_look_dir(look_at.sub(pos),1.0f);
	}
	
	public vector get_v()
	{
		return new vector(pos.sub(pos_before));
	}
	
	public void Move() {
		
		set_look_dir();
	
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			pos.set_x(pos.get_x()-get_speed() / MechaCube.get_FPS_M());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			pos.set_x(pos.get_x()+get_speed() / MechaCube.get_FPS_M());
			
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			pos.set_y(pos.get_y()+get_speed() / MechaCube.get_FPS_M());
			
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			pos.set_y(pos.get_y()-get_speed() / MechaCube.get_FPS_M());
		}
	}*/
}
