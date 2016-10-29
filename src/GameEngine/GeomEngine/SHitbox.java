package GameEngine.GeomEngine;

import GameEngine.BaseEngine.SObject;

public class SHitbox {
	private SObject owner;
	
	public SHitbox(SObject owner){
		this.owner = owner;
	}
	
	public SObject getOwner() {
		return owner;
	}
	public void setOwner(SObject owner) {
		this.owner = owner;
	}


	public static boolean intersects(SObject object1, SObject object2){
		SHitbox hitbox1 = object1.getHitbox();
		SHitbox hitbox2 = object2.getHitbox();
		if (hitbox1 instanceof SHitboxSpherical || hitbox2 instanceof SHitboxSpherical){
			if (hitbox1 instanceof SHitboxSpherical && hitbox2 instanceof SHitboxSpherical){
				if(object1.getPos().d(object2.getPos()) <=
					((SHitboxSpherical)hitbox1).getRadius()+((SHitboxSpherical)hitbox2).getRadius()){
					return true;
				}
				else{
					return false;
				}
				
			}
		}
		
		
		
		return false;
	}

}
