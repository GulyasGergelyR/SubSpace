package GameEngine.EntityEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import WebEngine.ComEngine.SMessage;
import GameEngine.BaseEngine.SMobile;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public class SHumanControl extends SControl{
	
private static final int numberofkeys = 4;
	
	private final String[] keyChars = {"W","A","S","D"};
	private boolean[] keyStates = new boolean[numberofkeys];
	private boolean[] prevKeyStates = new boolean[numberofkeys];
	
	public SHumanControl(SMobile mobile){
		super(mobile);
		for(int i=0;i<numberofkeys;i++)
		{
			keyStates[i] = false;
			prevKeyStates[i] = false;
		}
	}
	
	public boolean setKeyTo(String keyChar, boolean state)
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
		if (prevKeyStates[key] != state) return true; else return false;
	}
	
	@Override
	public void Think() {
		SVector acclDir = new SVector();
		String command = new String();
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			acclDir = acclDir.add(-1.0f, 0.0f);
			if (setKeyTo("A", true)) command  = command+"PA;";
		}else 
			if (setKeyTo("A", false)) command  = command+"RA;";
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			acclDir = acclDir.add(1.0f, 0.0f);
			if (setKeyTo("D", true)) command  = command+"PD;";
		}else 
			if (setKeyTo("D", false)) command  = command+"RD;";
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			acclDir = acclDir.add(0.0f, 1.0f);
			if (setKeyTo("W", true)) command  = command+"PD;";
		}else 
			if (setKeyTo("W", false)) command  = command+"RD;";
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			acclDir = acclDir.add(0.0f, -1.0f);
			if (setKeyTo("S", true)) command  = command+"PS;";
		}else 
			if (setKeyTo("S", false)) command  = command+"RS;";
		
		if (command.length()>0){
			System.out.println(command);
			SMessage message = new SMessage(Owner.getId(), "CCCCC", command);
			SMain.SendClientMessage(message);
		}
		
		if(acclDir.l()==0){
			Owner.setAcclDir(Owner.getMoveDir().setLength(-Owner.getMaxAcceleration()/2.0f));
		}
		else{
			Owner.setAcclDir(acclDir.setLength(Owner.getMaxAcceleration()));
		}
		Owner.Move();
		
		int M_x = Mouse.getX();
		int M_y = Mouse.getY();
		Owner.setLookDir(new SVector(M_x,M_y).sub(Owner.getPos()));
	}
}
