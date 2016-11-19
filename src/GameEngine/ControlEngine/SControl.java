package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;

public class SControl {
	protected SMobile Owner;
	
	public SControl(SMobile mobile){
		Owner = mobile;
	}
	public  boolean setKeyTo(int key, boolean state){return false;}
	protected void Think(){}
	protected void Act(){}
	public void ThinkAndAct(){}
}
