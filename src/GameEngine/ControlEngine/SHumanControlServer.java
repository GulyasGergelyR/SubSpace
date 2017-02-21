package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;


public class SHumanControlServer extends SControlServer{
	//keyChars = {"W","A","S","D","1","2","3","4","5"};
	private static final int numberofkeys = 9;
	private static final int numberofbuttons = 2;

	private boolean[] keyStates = new boolean[numberofkeys];
	private boolean[] mouseStates = new boolean[numberofbuttons];
	private boolean[] prevKeyStates = new boolean[numberofkeys];
	private boolean[] prevMouseStates = new boolean[numberofbuttons];
	
	public SHumanControlServer(SMobile mobile){
		super(mobile);
		for(int i=0;i<numberofkeys;i++){
			keyStates[i] = false;
			prevKeyStates[i] = false;
		}
		for(int i=0;i<numberofbuttons;i++){
			mouseStates[i] = false;
			prevMouseStates[i] = false;
		}
	}
	public boolean setKeyTo(int key, boolean state)
	{
		prevKeyStates[key] = keyStates[key];
		keyStates[key] = state;
		if (prevKeyStates[key] != state) return true; else return false;
	}
	public boolean setMouseTo(int key, boolean state)
	{
		prevMouseStates[key] = mouseStates[key];
		mouseStates[key] = state;
		if (prevMouseStates[key] != state) return true; else return false;
	}
	@Override
	protected void Think(){
		SVector acclDir = new SVector();
		if(keyStates[0]) acclDir = acclDir.add(0,1);
		if(keyStates[1]) acclDir = acclDir.add(-1,0);
		if(keyStates[2]) acclDir = acclDir.add(0,-1);
		if(keyStates[3]) acclDir = acclDir.add(1,0);
		
		if(acclDir.l()==0){
			if (Owner.getMoveDir().l() > 1)
				Owner.setAcclDir(Owner.getMoveDir().setLength(-Owner.getMaxAcceleration()/5.0f));
			else{
				Owner.setMoveDir(new SVector());
				Owner.setAcclDir(new SVector());
			}
		}else{
			float accl = Owner.getMaxAcceleration();
			//acclDir = acclDir.rotate(-Owner.getLookDir().getAngle()+90.0f);
			//float factor = 1/(1+Owner.getLookDir().getAbsAngleBetween(acclDir)/4.0f);
			Owner.setAcclDir(acclDir.setLength(accl));//*factor));
		}
		float angle = Owner.getAimLookDir().getAngle() - Owner.getLookDir().getAngle();
		float rotdir = 0;
		if (angle<0.0f)	{if (Math.abs(angle)<180.0f) rotdir = 1; else rotdir = -1;}
		else			{if (Math.abs(angle)<180.0f) rotdir = -1; else rotdir = 1;}
		Owner.setRotAcceleration(Owner.getMaxRotAcceleration()*rotdir);
	}
	@Override
	protected void Act() {
		super.Act();
		((SEntity)Owner).rechargeShield();
		// Firing weapon
		if (mouseStates[0]){
			((SEntity)Owner).tryToFire();
		}
	}
	
}
