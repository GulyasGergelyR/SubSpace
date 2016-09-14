package GameEngine.SyncEngine;



public class SFPS {
	private int fps;
	private long last_frame;
	private int last_fps;
	
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - last_frame);
		last_frame = time;

		return delta;
	}

	public long getTime() {
		//return (GLFW.glfwGetTime() * 1000) / GLFW.Sys.getTimerResolution();
		return 0;
	}

	public void updateFPS() {
		
		if (getTime() - last_fps > 1000) {

			fps = 0;
			last_fps += 1000;
		}
		fps++;
	}
}
