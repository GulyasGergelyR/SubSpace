package GameEngine.EntityEngine;

import GameEngine.BaseEngine.SMobile;

public class SControl {
	protected SMobile Owner;
	
	public SControl(SMobile mobile){
		Owner = mobile;
	}
	public  boolean setKeyTo(String key, boolean state){return false;}
	protected void Think(){}
	protected void Act(){
		Owner.Move();
		Owner.Rotate();
	}
	
	public void ThinkAndAct(){
		Think();
		if(!Owner.IsPosUpdated()){
			Act();
		}
	}
}
