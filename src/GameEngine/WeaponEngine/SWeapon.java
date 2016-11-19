package GameEngine.WeaponEngine;

import GameEngine.EntityEngine.SEntity;

public class SWeapon {
	private SEntity Owner;
	private int coolTime = 25;
	private int clipSize = 0; // zero means it does not have to be reloaded
	private int ammo = 0;  // zero means there is infinite number of bullets
	private Class bulletClass;
	
	public SWeapon(SEntity owner){
		this.Owner = owner;
	}
	
	
}
