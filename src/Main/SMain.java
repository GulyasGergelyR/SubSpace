package Main;

import javax.swing.JOptionPane;

import GameEngine.SGameInstance;
import WebEngine.Client;
import WebEngine.Server;

public class SMain {
	
	private static SGameInstance gameInstance;
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
	}
	
	private static void StartServer(){
		
	}
	private static void StartClient(){
		while(true){
			System.out.println(getDelta()+" "+getDeltaRatio());
			for(long i=0;i<100000000;i++){
				
			}
			updateDelta();
		}
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
}
