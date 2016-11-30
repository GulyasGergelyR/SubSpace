package GameEngine.ObjectEngine.PowerUpEngine;

import GameEngine.SId;
import GameEngine.ControlEngine.SControl;
import GameEngine.ControlEngine.SPowerUpControlServer;
import GameEngine.GeomEngine.SVector;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SPowerUpFactory {
	public static final byte PowerUpHeal = 1;
	protected static int currentNumberOfPowerUps = 0;
	protected static final int NumberOfPowerUps = 6;
	
	public static void createNewPowerUpAtClient(SVector pos, int id, byte powerUpType){
		if (powerUpType == PowerUpHeal){
			SPowerUpHeal powerUpHeal = new SPowerUpHeal(pos);
			powerUpHeal.setController(new SControl(powerUpHeal));
			powerUpHeal.setId(new SId(id));
			SMain.getGameInstance().addObject(powerUpHeal);
		}
	}
	public static void tryToCreateNewPowerUpAtServer(SVector pos, byte powerUpType){
		if (currentNumberOfPowerUps >= NumberOfPowerUps)
			return;
		if (powerUpType == PowerUpHeal){
			SPowerUpHeal powerUpHeal = new SPowerUpHeal(pos);
			powerUpHeal.setController(new SPowerUpControlServer(powerUpHeal));
			SMain.getGameInstance().addObject(powerUpHeal);
			SM message = SMPatterns.getObjectCreateMessage(powerUpHeal);
			SMain.getCommunicationHandler().SendMessage(message);
			currentNumberOfPowerUps++;
		}
	}
	public static void powerUpApplied(){
		currentNumberOfPowerUps--;
	}
}
