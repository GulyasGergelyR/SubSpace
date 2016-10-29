package GameEngine.EntityEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;
import GameEngine.GeomEngine.SVector;
import Main.SMain;
import WebEngine.ComEngine.SMessage;
import WebEngine.ComEngine.SCommunicationHandler.UDPNodeRole;

public class SHumanControlLocal extends SHumanControl{
	
	public SHumanControlLocal(SMobile mobile) {
		super(mobile);
	}
	
	public void stuff(){}
	
	@Override
	protected void Think() {
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
			SMessage message = new SMessage(Owner.getId(), "CLIIN", command);
			SMain.getCommunicationHandler().SendMessage(message);
		}
	}
}
