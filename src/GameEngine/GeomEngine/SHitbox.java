package GameEngine.GeomEngine;

import GameEngine.BaseEngine.SObject;

public class SHitbox {
	protected SObject Owner;
	
	public SHitbox(SObject owner){
		this.Owner = owner;
	}
	
	public SObject getOwner() {
		return Owner;
	}
	public void setOwner(SObject owner) {
		this.Owner = owner;
	}
	
	public SHitbox SHCopy(SObject owner){
		return new SHitbox(owner);
	}

}
