package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;


public class SControlClient<T extends SMobile> extends SControl<T>{
	public SControlClient(T mobile){
		super(mobile);
	}

	@Override
	public void ThinkAndAct() {
		Think();
	}
}
