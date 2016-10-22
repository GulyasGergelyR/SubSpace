package GameEngine.EntityEngine;

import GameEngine.BaseEngine.SMobile;

public class SControl {
	protected SMobile Owner;
	
	public SControl(SMobile mobile){
		Owner = mobile;
	}
	
	public void Think(){
		Owner.Move();
		Owner.Rotate();
	}
}
