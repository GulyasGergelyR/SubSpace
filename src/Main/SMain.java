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
import GameEngine.Specifications;
import GameEngine.EntityEngine.SEntity;
import GameEngine.SyncEngine.SServerTimer;
import RenderingEngine.SRenderer;
import WebEngine.SUDPClient;
import WebEngine.SUDPServer;
import WebEngine.ComEngine.SCommunicationHandler;
import WebEngine.ComEngine.SMessage;

public class SMain {
	
	private static SGameInstance gameInstance;
	private static SCommunicationHandler communicationHandler;
	private static SRenderer renderer;
	
	
	private static SUDPServer server;
	private static SUDPClient client;
	
	
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
				InitServer();
				server = new SUDPServer(9090,9089);
				StartServer(server);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if (server != null)
					server.Close();
				e.printStackTrace();
			} finally {
				if (server != null)
					server.Close();
			}
		}
		else{
			try {
				InitClient();
				client = new SUDPClient(9089,9090);
				StartClient();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				if(client!=null)
					client.Close();
				e.printStackTrace();
			} finally {
				if(client!=null)
					client.Close();
			}
		}
	}

	private static void InitServer(){
		Specifications.InitSpecifications();
		gameInstance = new SGameInstance();
		communicationHandler = new SCommunicationHandler();
	}
	private static void InitClient(){
		Specifications.InitSpecifications();
		gameInstance = new SGameInstance();
		communicationHandler = new SCommunicationHandler();
		
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
		SServerTimer timer = new SServerTimer();
		while(true){
			timer.StartTimer();
			gameInstance.CheckClientMessages();
			gameInstance.UpdateEntities();
			//write outputs
			timer.SleepIfRequired();
			updateDelta();
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
	
	public static void SendClientMessage(SMessage message){
		try {
			client.SendMessage(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static SUDPClient getUDPClient(){
		return client;
	}
	
	public static SGameInstance getGameInstance(){
		return gameInstance;
	}
	
	public static SCommunicationHandler getCommunicationHandler(){
		return communicationHandler;
	}
	
	private static void updateDelta(){
		gameInstance.updateDelta();
	}
	
	public static int getDelta(){
		return gameInstance.getDelta();
	}
	
	public static float getDeltaRatio(){
		return gameInstance.getDeltaRatio();
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
