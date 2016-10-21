package Main;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import GameEngine.SGameInstance;
import RenderingEngine.SRenderer;
import WebEngine.Client;
import WebEngine.Server;

public class SMain {
	
	private static SGameInstance gameInstance;
	private static SRenderer renderer;
	private static int delta;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Object[] options = {"Start Server",
                "Start Client"};
		int n = JOptionPane.showOptionDialog(null,
				"Start As",
				"SubSpace",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		
		if (n == 0){
			// Start server
			Server server = new Server(9090);
			InitServer();
			StartServer();
		}
		else{
			// Start client
			Client client = new Client("127:0:0:1",9090);
			InitClient();
			StartClient();
		}
	}
	
	private static void InitServer(){
		gameInstance = new SGameInstance();
	}
	private static void InitClient(){
		gameInstance = new SGameInstance();
		renderer = new SRenderer(gameInstance);
		
		try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
 
        initGL(); // init OpenGL
	}
	
	private static void StartServer(){
		
	}
	private static void StartClient(){
		while (!Display.isCloseRequested()) {
			System.out.println(getDelta()+" "+getDeltaRatio());
			renderer.DrawObjects();
			Display.update();
            Display.sync(60); // cap fps to 60fps
			updateDelta();
		}
		Display.destroy();
	}

	
	public static SGameInstance getGameInstance(){
		return gameInstance;
	}
	
	private static void updateDelta(){
		delta = gameInstance.getFPS().getDelta();
	}
	
	public static int getDelta(){
		return delta;
	}
	
	public static float getDeltaRatio(){
		return ((float)delta)/gameInstance.getFPS().getFPS_M();
	}
	
	public static void initGL() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 0, 600, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
