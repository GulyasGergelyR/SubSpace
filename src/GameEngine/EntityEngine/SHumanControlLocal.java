package GameEngine.EntityEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;
import GameEngine.GeomEngine.SVector;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SHumanControlLocal extends SHumanControl{
	
	private int keys[] = {Keyboard.KEY_W, Keyboard.KEY_A, Keyboard.KEY_S, Keyboard.KEY_D};
	
	public SHumanControlLocal(SMobile mobile) {
		super(mobile);
	}
	/*
	protected void ThinkOld() {
		
		String command = new String();
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (setKeyTo("A", true)) command  = command+"PA;";
		}else if (setKeyTo("A", false)) command  = command+"RA;";
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if (setKeyTo("D", true)) command  = command+"PD;";
		}else if (setKeyTo("D", false)) command  = command+"RD;";
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			if (setKeyTo("W", true)) command  = command+"PW;";
		}else if (setKeyTo("W", false)) command  = command+"RW;";
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			if (setKeyTo("S", true)) command  = command+"PS;";
		}else if (setKeyTo("S", false)) command  = command+"RS;";
		
		int M_x = Mouse.getX();
		int M_y = Mouse.getY();
		
		Owner.setAimLookDir(new SVector(M_x-Specifications.WindowWidth/2,
				M_y-Specifications.WindowHeight/2));
		command += "mp;"+Owner.getAimLookDir().getString()+";";
		
		//if(SMain.getCommunicationHandler().getUDPNodeRole().equals(UDPNodeRole.Server))
		//	super.Think();
		
		if (command.length()>0){
			SM message = new SM();
			SMain.getCommunicationHandler().SendMessage(message);
		}
	}
	*/
	
	@Override
	protected void Think() {
		byte command = 0;  //WASD - MPL - MPR
		boolean change = false;
		// W:0 A:1 S:2 D:3
		for (int key=0;key<4;key++){
			if (Keyboard.isKeyDown(keys[key])) {
				if (setKeyTo(key, true)) {
					command += 1<<key;
					change=true;
				}
			}
			else if (setKeyTo(key, false)) {
					change=true;
			}
		}
		int M_x = Mouse.getX();
		int M_y = Mouse.getY();
		
		Owner.setAimLookDir(new SVector(M_x-Specifications.WindowWidth/2,
				M_y-Specifications.WindowHeight/2));
		
		System.out.println("Command: "+command);
		
		if (change){
			SM message = SMPatterns.getClientUpdateMessage(Owner, command);
			SMain.getCommunicationHandler().SendMessage(message);
		}
	}
}
