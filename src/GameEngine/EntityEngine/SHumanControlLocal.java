package GameEngine.EntityEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GameEngine.BaseEngine.SMobile;
import GameEngine.GeomEngine.SVector;
import Main.SMain;
import WebEngine.ComEngine.SMessage;

public class SHumanControlLocal extends SHumanControl{
	
	public SHumanControlLocal(SMobile mobile) {
		super(mobile);
	}
	
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
			if (setKeyTo("W", true)) command  = command+"PD;";
		}else if (setKeyTo("W", false)) command  = command+"RD;";
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			if (setKeyTo("S", true)) command  = command+"PS;";
		}else if (setKeyTo("S", false)) command  = command+"RS;";
		super.Think();

		int M_x = Mouse.getX();
		int M_y = Mouse.getY();
		Owner.setLookDir(new SVector(M_x,M_y).sub(Owner.getPos()));
		
		if (command.length()>0){
			SMessage message = new SMessage(Owner.getId(), "ENTIN", command);
			SMain.getCommunicationHandler().SendMessage(message);
		}
	}
}
