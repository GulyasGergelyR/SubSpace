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
	protected static final int maxNumberOfPowerUps = 10;
	
	public static void createNewPowerUpAtClient(SVector pos, int id, byte powerUpType){
		SPowerUp powerUp = new SPowerUp(new SVector(0,0));
		if (powerUpType == PowerUpHeal){
			powerUp = new SPowerUpHeal(pos);
		} else if (powerUpType == PowerUpBurst){
			powerUp = new SPowerUpBurst(pos);
		} else if (powerUpType == PowerUpForceBoost){
			powerUp = new SPowerUpForceBoost(pos);
		}
		powerUp.setController(new SControl(powerUp));
		powerUp.setId(new SId(id));
		addObject(powerUp);
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
			addObject(powerUpHeal);
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
		} else if (powerUpType == PowerUpForceBoost) {
			if (SPowerUpForceBoost.currentNumberOfPowerUps >= SPowerUpForceBoost.maxNumberOfPowerUps)
				return;
			SPowerUpForceBoost powerUpForceBoost = new SPowerUpForceBoost(pos);
			addObject(powerUpForceBoost);
			SM message = SMPatterns.getObjectCreateMessage(powerUpForceBoost);
			SMain.getCommunicationHandler().SendMessage(message);
			SPowerUpForceBoost.currentNumberOfPowerUps++;
		}
	}
	public static void powerUpApplied(byte powerUpType){
		if (powerUpType == PowerUpHeal){
			SPowerUpHeal.currentNumberOfPowerUps--;
		} else if (powerUpType == PowerUpBurst) {
			SPowerUpBurst.currentNumberOfPowerUps--;
		} else if (powerUpType == PowerUpForceBoost) {
			SPowerUpForceBoost.currentNumberOfPowerUps--;
		}
	}
}
