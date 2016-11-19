package GameEngine.WeaponEngine;

import GameEngine.EntityEngine.SEntity;
import Main.SMain;

public class SWeapon{
	protected SEntity owner;
	protected int coolTime = 25;
	protected int lastTime = 0;
	protected int clipSize = 0; // zero means it does not have to be reloaded
	protected int maxAmmo = 0; // zero means there is infinite number of bullets
	protected int ammoInClip = 0; 
	protected int ammo = 0;  
	
	protected SBullet baseBullet = new SBullet(owner);
	
	public SWeapon(SEntity owner){
		this.owner = owner;
	}
	
	public SBullet getBaseBullet() {
		return baseBullet;
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
