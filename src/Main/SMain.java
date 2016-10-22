package Main;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import GameEngine.SGameInstance;
import GameEngine.SResLoader;
import GameEngine.EntityEngine.SEntity;
import RenderingEngine.SRenderer;
import WebEngine.SUDPServer;

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
			try {
				SUDPServer server = new SUDPServer(9090);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			InitServer();
			StartServer(server);
		}
		else{
			// Start client
			
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
            Display.setDisplayMode(new DisplayMode(1024, 768));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
 
        initGL(); // init OpenGL
        initResources();
        
        gameInstance.addEntity(new SEntity());
	}
	
	private static void initResources() {
		// TODO Auto-generated method stub
		String[] res = new String[4];
		res[0] = "res/entity/spaceshipv1.png";
		res[1] = "res/entity/prob.png";
		res[2] = "res/dot.png";
		res[3] = "res/entity/spaceshipv2.png";
		SResLoader.addSpriteArray(res);
	}

	private static void StartServer(SUDPServer server){
		while(true){
			gameInstance.UpdateEntities();
		}
		
		
	}
	private static void StartClient(){
		while (!Display.isCloseRequested()) {
			gameInstance.UpdateEntities();
			renderGL();
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
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 1024, 0, 768, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void renderGL() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		renderer.DrawObjects();
	}
}
