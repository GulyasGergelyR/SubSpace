package GameEngine.WeaponEngine;

import java.util.Random;

import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFH;

public class SWeapon{
	protected SEntity Owner;
	protected int coolTime = 7;
	protected int lastTime = 7;
	protected int clipSize = 0; // zero means it does not have to be reloaded
	protected int maxAmmo = 0; // zero means there is infinite number of bullets
	protected int ammoInClip = 0; 
	protected int ammo = 0;  
	
	protected SBullet baseBullet;
	
	protected boolean burstMode;  // for burst effect
	
	public SWeapon(SEntity owner){
		this.Owner = owner;
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
			for(int i=0;i<4;i++){
				Random random = new Random();
				SBullet bullet = baseBullet.createBullet();
				bullet.setMoveDir(bullet.getMoveDir().rotate(random.nextFloat()*45.0f-22.5f));
				bullet.setLookDir(new SVector(bullet.getMoveDir()));
				SFH.Bullets.createNewBulletAtServer(bullet);
			}
		} else{
			SBullet bullet = baseBullet.createBullet();
			SFH.Bullets.createNewBulletAtServer(bullet);
		}
		lastTime = 0;
	}

	public void setBurstMode(boolean burstMode) {
		this.burstMode = burstMode;
	}

	public int getCoolTime() {
		return coolTime;
	}

	public void setCoolTime(int coolTime) {
		this.coolTime = coolTime;
	}
	
	
}
