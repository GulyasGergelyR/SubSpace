package GameEngine.EntityEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GameEngine.BaseEngine.SMobile;
import GameEngine.GeomEngine.SVector;


public class SDistantHumanControl extends SControl{
	public static final int W = 0;
	public static final int A = 1;
	public static final int S = 2;
	public static final int D = 3;
	
	private static final int numberofkeys = 4;
	
	private final String[] keyChars = {"W","A","S","D"};
	private boolean[] keyStates;
	private boolean[] prevKeyStates;
	
	public SDistantHumanControl(SMobile mobile){
		super(mobile);
		for(int i=0;i<numberofkeys;i++)
			{
				keyStates[i] = false;
				prevKeyStates[i] = false;
			}
	}
	
	
	public void setKeyTo(int key, boolean state)
	{
		prevKeyStates[key] = keyStates[key];
		keyStates[key] = state;
	}
	public void setKeyTo(String keyChar, boolean state)
	{
		int key = -1;
		for(int i=0;i<numberofkeys; i++){
			if(keyChars[i]==keyChar){
				key = i;
				break;
			}
		}
		prevKeyStates[key] = keyStates[key];
		keyStates[key] = state;
	}
	
	@Override
	public void Think() {
		SVector acclDir = new SVector();
		if(keyStates[W]) acclDir.add(new SVector(0,1));
		if(keyStates[A]) acclDir.add(new SVector(-1,0));
		if(keyStates[S]) acclDir.add(new SVector(0,-1));
		if(keyStates[D]) acclDir.add(new SVector(1,0));
		
		if(acclDir.l()==0){
			Owner.setAcclDir(Owner.getMoveDir().setLength(-Owner.getMaxAcceleration()/2.0f));
		}
		else{
			Owner.setAcclDir(acclDir.setLength(Owner.getMaxAcceleration()));
		}
		Owner.Move();
		//TODO add rotation
	}
	
}
