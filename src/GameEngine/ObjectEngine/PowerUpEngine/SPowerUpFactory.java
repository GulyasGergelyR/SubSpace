package GameEngine.ObjectEngine.PowerUpEngine;

import java.util.Random;

import GameEngine.SId;
import GameEngine.ControlEngine.SControl;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFactory;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SPowerUpFactory extends SFactory{
	public static final byte PowerUpHeal = 1;
	public static final byte PowerUpBurst = 2;
	public static final byte PowerUpForceBoost = 3;
	
	protected static int currentNumberOfPowerUps = 0;
	protected static final int maxNumberOfPowerUps = 3;
	
	public static void createNewPowerUpAtClient(SVector pos, int id, byte powerUpType){
		if (powerUpType == PowerUpHeal){
			SPowerUpHeal powerUpHeal = new SPowerUpHeal(pos);
			powerUpHeal.setController(new SControl(powerUpHeal));
			powerUpHeal.setId(new SId(id));
			SMain.getGameInstance().addObject(powerUpHeal);
		} else if (powerUpType == PowerUpBurst){
			SPowerUpBurst powerUpBurst = new SPowerUpBurst(pos);
			powerUpBurst.setController(new SControl(powerUpBurst));
			powerUpBurst.setId(new SId(id));
			addObject(powerUpBurst);
		}
	}
	public static void tryToCreateNewPowerUpAtServer(byte powerUpType){
		Random random = new Random();
		SVector pos = new SVector(random.nextFloat()*8000 -4000, random.nextFloat()*8000 -4000);
		if (currentNumberOfPowerUps >= maxNumberOfPowerUps)
			return;
		if (powerUpType == PowerUpHeal){
			if (SPowerUpHeal.currentNumberOfPowerUps >= SPowerUpHeal.maxNumberOfPowerUps)
				return;
			SPowerUpHeal powerUpHeal = new SPowerUpHeal(pos);
			SMain.getGameInstance().addObject(powerUpHeal);
			SM message = SMPatterns.getObjectCreateMessage(powerUpHeal);
			SMain.getCommunicationHandler().SendMessage(message);
			SPowerUpHeal.currentNumberOfPowerUps++;
		} else if (powerUpType == PowerUpBurst) {
			if (SPowerUpBurst.currentNumberOfPowerUps >= SPowerUpBurst.maxNumberOfPowerUps)
				return;
			SPowerUpBurst powerUpBurst = new SPowerUpBurst(pos);
			addObject(powerUpBurst);
			SM message = SMPatterns.getObjectCreateMessage(powerUpBurst);
			SMain.getCommunicationHandler().SendMessage(message);
			SPowerUpBurst.currentNumberOfPowerUps++;
		}
	}
	public static void powerUpApplied(byte powerUpType){
		if (powerUpType == PowerUpHeal){
			SPowerUpHeal.currentNumberOfPowerUps--;
		} else if (powerUpType == PowerUpBurst) {
			SPowerUpBurst.currentNumberOfPowerUps--;
		}
	}
}
