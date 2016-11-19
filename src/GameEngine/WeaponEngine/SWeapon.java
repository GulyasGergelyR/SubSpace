package GameEngine.WeaponEngine;

import GameEngine.EntityEngine.SEntity;
import Main.SMain;

public class SWeapon{
	private SEntity owner;
	private int coolTime = 25;
	private int lastTime = 0;
	private int clipSize = 0; // zero means it does not have to be reloaded
	private int maxAmmo = 0; // zero means there is infinite number of bullets
	private int ammoInClip = 0; 
	private int ammo = 0;  
	
	private SBullet baseBullet = new SBullet(owner);
	
	public SWeapon(SEntity owner){
		this.owner = owner;
	}
	
	public void tryIt(){
		if (cooled()){
			fireIt();
		}
	}
	
	public void fireIt(){
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
	}
}
