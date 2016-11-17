package GameEngine.EntityEngine;

import java.util.HashMap;
import java.util.Map;

import GameEngine.BaseEngine.SMobile;
import GameEngine.GeomEngine.SVector;


public class SHumanControl extends SControl{
	private static final String[] keyChars = {"W","A","S","D","1","2","3","4","5"};
	private static final int numberofkeys = 9;
	
	//private Map<String, Boolean> keyStates = new HashMap<String, Boolean>();
	//private Map<String, Boolean> prevKeyStates = new HashMap<String, Boolean>();
	private boolean[] keyStates = new boolean[9];
	private boolean[] prevKeyStates = new boolean[9];

	public SHumanControl(SMobile mobile){
		super(mobile);
		for(int i=0;i<numberofkeys;i++){
				keyStates[i] = false;
				prevKeyStates[i] = false;
			}
	}
	public boolean setKeyTo(int key, boolean state)
	{
		prevKeyStates[key] = keyStates[key];
		keyStates[key] = state;
		if (prevKeyStates[key] != state) return true; else return false;
	}
	
	@Override
	protected void Think(){
		SVector acclDir = new SVector();
		if(keyStates[0]) acclDir = acclDir.add(0,1);
		if(keyStates[1]) acclDir = acclDir.add(-1,0);
		if(keyStates[2]) acclDir = acclDir.add(0,-1);
		if(keyStates[3]) acclDir = acclDir.add(1,0);
		
		if(acclDir.l()==0){
			Owner.setAcclDir(Owner.getMoveDir().setLength(-Owner.getMaxAcceleration()/10.0f));
		}else{
			float accl = Owner.getMaxAcceleration();
			float factor = 1/(1+Owner.getLookDir().getAbsAngleBetween(acclDir)/4.0f);
			Owner.setAcclDir(acclDir.setLength(accl*factor));
		}
		float angle = Owner.getAimLookDir().getAngle() - Owner.getLookDir().getAngle();
		float rotdir = 0;
		if (angle<0.0f)	{if (Math.abs(angle)<180.0f) rotdir = 1; else rotdir = -1;}
		else			{if (Math.abs(angle)<180.0f) rotdir = -1; else rotdir = 1;}
		Owner.setRotAcceleration(Owner.getMaxRotAcceleration()*rotdir);
	}
}
