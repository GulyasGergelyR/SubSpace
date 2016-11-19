package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;

public class SControlServer extends SControl{
	public SControlServer(SMobile mobile){
		super(mobile);
	}
	@Override
	protected void Act() {
		Owner.Move();
		Owner.Rotate();
	}
	@Override
	public void ThinkAndAct() {
		Think();
		Act();
	}
}
