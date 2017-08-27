package GameEngine.ControlEngine;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import GameEngine.Specifications;
import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SEntity.PlayerGameState;
import GameEngine.GeomEngine.SVector;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SHumanControlClient extends SControlClient<SEntity>{
	
	private int keys[] = {Keyboard.KEY_W, Keyboard.KEY_A, Keyboard.KEY_S, Keyboard.KEY_D};
	private int spawnCounter = 0;
	
	public SHumanControlClient(SEntity mobile) {
		super(mobile);
		try {
			Cursor emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
			Mouse.setNativeCursor(emptyCursor);
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		if (Owner.getPlayerGameState().equals(PlayerGameState.Respawning)){
			spawnCounter++;
			if (spawnCounter >= 30) {
				spawnCounter = 0;
			}
			if ((spawnCounter / 15 ) % 2 == 0){
				Owner.getBody().setTransparency(0.5f);
			} else {
				Owner.getBody().setTransparency(1.0f);
			}
		} else if (Owner.getPlayerGameState().equals(PlayerGameState.Dead)){
			spawnCounter = 0;
		}
	}
}
