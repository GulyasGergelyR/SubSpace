package GameEngine.WeaponEngine;

import GameEngine.EntityEngine.SEntity;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SWeapon{
	protected SEntity owner;
	protected int coolTime = 7;
	protected int lastTime = 7;
	protected int clipSize = 0; // zero means it does not have to be reloaded
	protected int maxAmmo = 0; // zero means there is infinite number of bullets
	protected int ammoInClip = 0; 
	protected int ammo = 0;  
	
	protected SBullet baseBullet;
	
	protected boolean burstMode;  // for burst effect
	
	public SWeapon(SEntity owner){
		this.owner = owner;
		baseBullet = new SBullet(owner);
	}
	
	public SBullet getBaseBullet() {
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
		if (burstMode){
			for(int i=0;i<5;i++){
				SBullet bullet = baseBullet.createBullet();
				bullet.setMoveDir(bullet.getMoveDir().rotate((i-2)*15.0f));
				SMain.getGameInstance().addObject(bullet);
				SM message = SMPatterns.getObjectCreateMessage(bullet);
				SMain.getCommunicationHandler().SendMessage(message);
			}
		} else{
			SBullet bullet = baseBullet.createBullet();
			SMain.getGameInstance().addObject(bullet);
			SM message = SMPatterns.getObjectCreateMessage(bullet);
			SMain.getCommunicationHandler().SendMessage(message);
		}
		lastTime = 0;
	}

	public void setBurstMode(boolean burstMode) {
		this.burstMode = burstMode;
	}
	
	
}
