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

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import GameEngine.SGameInstance;
import GameEngine.SPlayer;
import GameEngine.SResLoader;
import GameEngine.Specifications;
import GameEngine.EntityEngine.SEntity;
import GameEngine.SyncEngine.SServerTimer;
import RenderingEngine.SRenderer;
import WebEngine.ComEngine.SCommunicationHandler;
import WebEngine.ComEngine.SNode;

public class SMain {
	
	private static SGameInstance gameInstance;
	private static SCommunicationHandler communicationHandler;
	private static SRenderer renderer;
	
	
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
				StartServer();
			} catch (Exception e) {
				if (communicationHandler != null)
					communicationHandler.CloseUDPNode();
				e.printStackTrace();
			} finally {
				if (communicationHandler != null)
					communicationHandler.CloseUDPNode();
			}
		}
		else{
			try {
				InitClient();
				
				StartClient();
			} catch (Exception e) {
				if (communicationHandler != null)
					communicationHandler.CloseUDPNode();
				e.printStackTrace();
			} finally {
				if (communicationHandler != null)
					communicationHandler.CloseUDPNode();
			}
		}
	}
	
	private static void Init(){
		Specifications.InitSpecifications();
		gameInstance = new SGameInstance();
		communicationHandler = new SCommunicationHandler();
	}
	
	private static void InitServer(){
		Init();
		
		SNode node;
		try {
			node = new SNode(InetAddress.getLocalHost(), 0);
			communicationHandler.setLocalNode(node);
			communicationHandler.createUDPNodeAsServer(9090, 9089);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void InitClient(){
		Init();
		SNode node;
		SPlayer player;
		SEntity entity;
		try {
			node = new SNode(InetAddress.getLocalHost(), 0);
			player = new SPlayer(node, "Gergo");
			entity = new SEntity();
			player.setEntity(entity);
			communicationHandler.setLocalNode(node);
			communicationHandler.createUDPNodeAsClient(9089, 9090);
			communicationHandler.ConnectToServer(node);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		SResLoader.addSpriteArray(Specifications.resourcePathStrings);
	}

	private static void StartServer(){
		SServerTimer timer = new SServerTimer();
		while(true){
			timer.StartTimer();
			//gameInstance.CheckClientMessages();
			communicationHandler.RequestPingDataFromClients();
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
