package GameEngine.SyncEngine;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import GameEngine.Specifications;



public class SFPS {
	private int fps;
	private long last_frame;
	private int last_fps;
	private int FPS_M;
	
	public SFPS(){
		FPS_M = Specifications.FPS_M;
		last_frame = getTime();
	}
	
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - last_frame);
		last_frame = time;

		return delta;
	}

	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public void updateFPS() {
		
		if (getTime() - last_fps > 1000) {
			Display.setTitle(""+fps);
			fps = 0;
			last_fps += 1000;
		}
		fps++;
	}
	public int getFPS_M(){
		return FPS_M;
	}
}
