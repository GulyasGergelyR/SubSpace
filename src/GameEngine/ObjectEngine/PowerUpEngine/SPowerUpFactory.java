package GameEngine.ObjectEngine.PowerUpEngine;

import java.util.Random;

import GameEngine.SId;
import GameEngine.ControlEngine.SControl;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFactory;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SPowerUpFactory extends SFactory<SPowerUp>{
	public static final byte PowerUpHeal = 1;
	public static final byte PowerUpBurst = 2;
	public static final byte PowerUpForceBoost = 3;
	
	protected int currentNumberOfPowerUps = 0;
	protected final int maxNumberOfPowerUps = 10;
	
	public SPowerUpFactory(){
		super();
		this.FactoryName = "Power-up factory";
	}
	
	public void createNewPowerUpAtClient(SVector pos, int id, byte powerUpType){
		SPowerUp powerUp = null;
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
	
	public void tryToCreateNewPowerUpAtServer(byte powerUpType){
		Random random = new Random();
		SVector pos = new SVector(random.nextFloat()*8000 -4000, random.nextFloat()*8000 -4000);
		if (currentNumberOfPowerUps >= maxNumberOfPowerUps)
			return;
		SPowerUp powerUp = null;
		if (powerUpType == PowerUpHeal){
			if (SPowerUpHeal.currentNumberOfPowerUps >= SPowerUpHeal.maxNumberOfPowerUps)
				return;
			powerUp = new SPowerUpHeal(pos);
		} else if (powerUpType == PowerUpBurst) {
			if (SPowerUpBurst.currentNumberOfPowerUps >= SPowerUpBurst.maxNumberOfPowerUps)
				return;
			powerUp = new SPowerUpBurst(pos);
		} else if (powerUpType == PowerUpForceBoost) {
			if (SPowerUpForceBoost.currentNumberOfPowerUps >= SPowerUpForceBoost.maxNumberOfPowerUps)
				return;
			powerUp = new SPowerUpForceBoost(pos);
		}
		addObject(powerUp);
		SM message = SMPatterns.getObjectCreateMessage(powerUp);
		SMain.getCommunicationHandler().SendMessage(message);
		SPowerUpHeal.currentNumberOfPowerUps++;
	}
	
	public void powerUpApplied(byte powerUpType){
		if (powerUpType == PowerUpHeal){
			SPowerUpHeal.currentNumberOfPowerUps--;
		} else if (powerUpType == PowerUpBurst) {
			SPowerUpBurst.currentNumberOfPowerUps--;
		} else if (powerUpType == PowerUpForceBoost) {
			SPowerUpForceBoost.currentNumberOfPowerUps--;
		}
	}
}
