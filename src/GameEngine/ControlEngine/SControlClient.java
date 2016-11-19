package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;

public class SControlClient extends SControl{
	public SControlClient(SMobile mobile){
		super(mobile);
	}

	@Override
	public void ThinkAndAct() {
		Think();
	}
}
