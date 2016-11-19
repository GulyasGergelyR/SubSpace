package GameEngine.WeaponEngine;

import GameEngine.EntityEngine.SEntity;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SWeapon{
	protected SEntity owner;
	protected int coolTime = 5;
	protected int lastTime = 5;
	protected int clipSize = 0; // zero means it does not have to be reloaded
	protected int maxAmmo = 0; // zero means there is infinite number of bullets
	protected int ammoInClip = 0; 
	protected int ammo = 0;  
	
	protected SBullet baseBullet;
	
	public SWeapon(SEntity owner){
		this.owner = owner;
		baseBullet = new SBullet(owner);
	}
	
	protected SBullet getBaseBullet() {
		return baseBullet;
	}
	
	public boolean tryIt(){
		if (cooled()){
			fireIt();
			return true;
		}else{
			return false;
		}
	}
	public void coolIt(){
		lastTime++;
	}
	
	protected void fireIt(){
		if (maxAmmo > 0){
			if (ammoInClip > 0){
				createBullet();
				ammoInClip -= baseBullet.getNumberOfBulletsAtOnce();
			}
		}else{
			createBullet();
		}
	}
	
	private boolean cooled(){
		return lastTime >= coolTime;
	}
	
	private void createBullet(){
		SBullet bullet = baseBullet.createBullet();
		SMain.getGameInstance().addObject(bullet);
		SM message = SMPatterns.getObjectCreateMessage(bullet);
		SMain.getCommunicationHandler().SendMessage(message);
		lastTime = 0;
	}
}
