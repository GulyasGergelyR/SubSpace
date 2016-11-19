package GameEngine.EntityEngine;

import java.util.HashMap;
import java.util.Map;

import GameEngine.BaseEngine.SMobile;
import GameEngine.GeomEngine.SVector;


public class SHumanControl extends SControl{
	private static final String[] keyChars = {"W","A","S","D","1","2","3","4","5"};
	private static final int numberofkeys = keyChars.length;
	
	private Map<String, Boolean> keyStates = new HashMap<String, Boolean>();
	private Map<String, Boolean> prevKeyStates = new HashMap<String, Boolean>();
	
	public SHumanControl(SMobile mobile){
		super(mobile);
		for(int i=0;i<numberofkeys;i++){
				keyStates.put(keyChars[i], false);
				prevKeyStates.put(keyChars[i], false);
			}
	}
	public boolean setKeyTo(String key, boolean state)
	{
		prevKeyStates.replace(key, keyStates.get(key));
		keyStates.replace(key, state);
		if (prevKeyStates.get(key) != state) return true; else return false;
	}
	
	@Override
	protected void Think(){
		SVector acclDir = new SVector();
		if(keyStates.get("W")) acclDir = acclDir.add(0,1);
		if(keyStates.get("A")) acclDir = acclDir.add(-1,0);
		if(keyStates.get("S")) acclDir = acclDir.add(0,-1);
		if(keyStates.get("D")) acclDir = acclDir.add(1,0);
		
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
