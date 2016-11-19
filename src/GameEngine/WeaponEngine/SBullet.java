package GameEngine.WeaponEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;

public class SBullet extends SMobile{
	protected SEntity owner;
	protected int numberOfBulletsAtOnce = 1;
	
	protected SVector origin;
	protected int lifeTime = 1000;
	protected int lifeDistance = 100;
	
	public SBullet createBullet(){
		return new SBullet(owner);
	}
	public SBullet(SEntity owner){
		this.owner = owner;
	}
	public int getNumberOfBulletsAtOnce() {
		return numberOfBulletsAtOnce;
	}
}
