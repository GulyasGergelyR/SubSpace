package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;

public class SControlServer<T> extends SControl<T>{
	public SControlServer(T mobile){
		super(mobile);
	}
	@Override
	protected void Act() {
		((SMobile)Owner).Move();
		((SMobile)Owner).Rotate();
	}
	@Override
	public void ThinkAndAct() {
		Think();
		Act();
	}
}
