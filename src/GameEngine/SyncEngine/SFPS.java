package GameEngine.SyncEngine;

import org.lwjgl.Sys;

import GameEngine.Specifications;



public class SFPS {
	private long last_frame;
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
	
	public int getFPS_M(){
		return FPS_M;
	}
}
