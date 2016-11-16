package GameEngine.GeomEngine;

import GameEngine.BaseEngine.SObject;

public class SHitbox {
	protected SObject owner;
	
	public SHitbox(SObject owner){
		this.owner = owner;
	}
	
	public SObject getOwner() {
		return owner;
	}
	public void setOwner(SObject owner) {
		this.owner = owner;
	}
	
	public SHitbox SHCopy(SObject owner){
		return new SHitbox(owner);
	}

}
