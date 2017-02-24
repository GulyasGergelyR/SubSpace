package GameEngine.GeomEngine;

import GameEngine.BaseEngine.SObject;

public class SHitboxSpherical extends SHitbox {
	private float radius;
	public SHitboxSpherical(SObject owner, float r){
		super(owner);
		this.radius = r;
	}
	public float getRadius() {
		return radius*Owner.getBody().getScale();
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	@Override
	public SHitboxSpherical SHCopy(SObject owner) {
		return new SHitboxSpherical(owner, this.radius);
	}
		
}
