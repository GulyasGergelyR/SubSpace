package GameEngine.ControlEngine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;
import GameEngine.GeomEngine.SVector;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SHumanControlClient extends SControlClient{
	
	private int keys[] = {Keyboard.KEY_W, Keyboard.KEY_A, Keyboard.KEY_S, Keyboard.KEY_D};
	
	public SHumanControlClient(SMobile mobile) {
		super(mobile);
	}
	@Override
	protected void Think() {
		byte command = 0;  //WASD - MPL - MPR
		// W:0 A:1 S:2 D:3
		for (int key=0;key<4;key++){
			if (Keyboard.isKeyDown(keys[key])) {
				command += 1<<key;
			}
		}
		int M_x = Mouse.getX();
		int M_y = Mouse.getY();
		boolean leftPressed = false;
		boolean rightPressed = false;
		while(Mouse.next()){
			if(Mouse.getEventButton() == 0){
				leftPressed = true;
			}
			if(Mouse.getEventButton() == 1){
				rightPressed = true;
			}
		}
		
		if(Mouse.isButtonDown(0)){
			leftPressed = true;
		}
		if(Mouse.isButtonDown(1)){
			rightPressed = true;
		}
		if(leftPressed){
			command += 1<<4;
		}
		if(rightPressed){
			command += 1<<5;
		}
		SVector aimLookDir = new SVector(M_x-Specifications.WindowWidth/2, M_y-Specifications.WindowHeight/2);
		SM message = SMPatterns.getClientUpdateMessage(Owner, command, aimLookDir);
		SMain.getCommunicationHandler().SendMessage(message);
	}
}
