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

import GameEngine.SGameInstance;
import GameEngine.SPlayer.PlayerState;
import GameEngine.SResLoader;
import GameEngine.Specifications;
import GameEngine.SyncEngine.SServerTimer;
import RenderingEngine.SRenderer;
import WebEngine.ComEngine.SCommunicationHandler;
import WebEngine.ComEngine.SCommunicationHandler.UDPRole;
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
	
	public static void InitServer(){
		Init();
		System.out.println("Starting server...");
		SNode node;
		try {
			node = new SNode(InetAddress.getLocalHost(), 0, 1); // server hets special id 1
			communicationHandler.setLocalNode(node);
			communicationHandler.createUDPNodeAsServer(9090, 9089);
			System.out.println("Server is running...");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void InitClient(){
		Init();
		SNode node;
		SNode server;
		try {
			node = new SNode(InetAddress.getLocalHost(), 0, "Gergo", PlayerState.local);
			gameInstance.setLocalPlayer(node.getPlayer());
			communicationHandler.setLocalNode(node);
			communicationHandler.createUDPNodeAsClient(9089, 9090);
			byte[] ipAddr = new byte[]{(byte)192, (byte)168, 1, 104};
			server = new SNode(InetAddress.getByAddress(ipAddr), 0, 1);  // server hets special id 1
			communicationHandler.ConnectToServer(server);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		renderer = new SRenderer(gameInstance);
		
		try {
            Display.setDisplayMode(new DisplayMode(Specifications.WindowWidth, Specifications.WindowHeight));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
 
        initGL(); // init OpenGL
        initResources();
	}
	
	private static void initResources() {
		SResLoader.addSpriteArray(Specifications.resourcePathStrings);
	}

	public static void StartServer(){
		SServerTimer timer = new SServerTimer();
		while(true){
			timer.StartTimer();
			communicationHandler.RequestPingDataFromClients();
			gameInstance.CheckEntityMessages();
			gameInstance.UpdateEntities();
			gameInstance.SendGameDataToClients();
			timer.SleepIfRequired();
			updateDelta();
		}
	}
	public static void StartClient(){
		while (!Display.isCloseRequested()) {
			gameInstance.CheckEntityMessages();
			gameInstance.UpdateEntities();
			renderGL();
			Display.update();
            Display.sync(Specifications.FPS_M); // cap fps to 60fps
			updateDelta();
		}
		Display.destroy();
	}
	
	public static SGameInstance getGameInstance(){
		return gameInstance;
	}
	
	public static boolean IsServer(){
		return communicationHandler.getUDPRole().equals(UDPRole.Server);
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
		glOrtho(0, Specifications.WindowWidth, 0, Specifications.WindowHeight, -1, 1);
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
