package Main;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_GREATER;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glAlphaFunc;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.openal.SoundStore;

import GameEngine.SGameInstance;
import GameEngine.SPlayer.PlayerType;
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
				InitServer(true);
				StartServer(true);
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
				InitClient(new byte[]{(byte)192, (byte)168, 1, 104});
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
	
	public static void InitServer(boolean serverWindow){
		Init();
		System.out.println("Starting server...");
		SNode node;
		try {
			node = new SNode(InetAddress.getLocalHost(), 0, 1); // server gets special id 1
			communicationHandler.setLocalNode(node);
			communicationHandler.createUDPNodeAsServer(9090, 9089);
			System.out.println("Server is running...");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (serverWindow){

			try {
	            Display.setDisplayMode(new DisplayMode(Specifications.WindowWidth, Specifications.WindowHeight));
	            Display.create();
	        } catch (LWJGLException e) {
	            e.printStackTrace();
	            System.exit(0);
	        }
			initGL(); // init OpenGL
		    initResources();
			renderer = new SRenderer(gameInstance);
		}
	}
	public static void InitClient(byte[] ipAddr){
		Init();
		SNode node;
		SNode server;
		try {
			InetAddress address = InetAddress.getLocalHost();
			node = new SNode(address, 0, address.getHostName(), PlayerType.local);
			gameInstance.setLocalPlayer(node.getPlayer());
			communicationHandler.setLocalNode(node);
			communicationHandler.createUDPNodeAsClient(9089, 9090);
			//byte[] ipAddr = new byte[]{(byte)192, (byte)168, 1, 104};
			//byte[] ipAddr = new byte[]{(byte)134, (byte)255, (byte)89, (byte)249};
			server = new SNode(InetAddress.getByAddress(ipAddr), 0, 1);  // server gets special id 1
			communicationHandler.ConnectToServer(server);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			 DisplayMode[] modes = Display.getAvailableDisplayModes();
             int max = 0;
             DisplayMode biggest = new DisplayMode(Specifications.WindowWidth, Specifications.WindowHeight);
             for (int i=0;i<modes.length;i++) {
                 DisplayMode current = modes[i];
                 if (current.getWidth()*current.getHeight() > max){
                	 max = current.getWidth()*current.getHeight();
                	 biggest = current;
                 }
                 
             }
             System.out.println("Resolution: "+biggest.getWidth()+"x"+biggest.getHeight()+" "+biggest.getFrequency()+" Hz");
            //Display.setDisplayMode(new DisplayMode(Specifications.WindowWidth, Specifications.WindowHeight));
            Display.setDisplayMode(biggest);
            Specifications.WindowWidth = biggest.getWidth();
            Specifications.WindowHeight = biggest.getHeight();
            Display.setFullscreen(true);
            Display.setVSyncEnabled(true);
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
 
        initGL(); // init OpenGL
        initResources();
        renderer = new SRenderer(gameInstance);
	}
	
	private static void initResources() {
		SResLoader.addSpriteArray(Specifications.resourcePathStrings);
		SResLoader.addAudioArray(Specifications.audioPathStrings);
	}

	public static void StartServer(boolean serverWindow){
		SServerTimer timer = new SServerTimer();
		boolean run = true;
		while(run){
			timer.StartTimer();
			communicationHandler.RequestPingDataFromClients();
			gameInstance.UpdateGame();
			gameInstance.CheckMessages();
			gameInstance.SendGameDataToClients();
			if (serverWindow){
				renderGL();
				Display.update();
				Display.sync(Specifications.FPS_M);
				run = !Display.isCloseRequested();
			}else{
				timer.SleepIfRequired();
			}
			updateDelta();
		}
	}
	public static void StartClient(){
		SResLoader.getAudio("res/audio/ambient.wav").playAsMusic(1.0f, 0.05f, true);
		while (!Display.isCloseRequested()) {
			gameInstance.UpdateGame();
			gameInstance.CheckMessages();
			renderGL();
			Display.update();
            Display.sync(Specifications.FPS_M); // cap fps to 60fps
            SoundStore.get().poll(0);
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
		glOrtho(0, Specifications.WindowWidth, Specifications.WindowHeight, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND); 
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_ALPHA_TEST);
	}

	public static void renderGL() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glAlphaFunc(GL_GREATER, 0);
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		renderer.DrawGame();
	}
}
