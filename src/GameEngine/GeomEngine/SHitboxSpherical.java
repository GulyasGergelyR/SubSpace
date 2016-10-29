package GameEngine.GeomEngine;

import GameEngine.BaseEngine.SObject;

public class SHitboxSpherical extends SHitbox {
	private float radius;
	public SHitboxSpherical(SObject owner, float r){
		super(owner);
		this.radius = r;
	}
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
}
