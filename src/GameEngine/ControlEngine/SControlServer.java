package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;

public class SControlServer<T extends SMobile> extends SControl<T>{
	public SControlServer(T mobile){
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
