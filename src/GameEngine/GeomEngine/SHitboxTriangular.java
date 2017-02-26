package GameEngine.GeomEngine;

import GameEngine.BaseEngine.SObject;

public class SHitboxTriangular extends SHitbox {

	protected SVector p1, p2, p3;
	
	public SHitboxTriangular(SObject owner, float m) {
		super(owner);
		SVector v1 = new SVector(0, 1);
		SVector v2 = new SVector(-1, 0);
		SVector v3 = new SVector(1, 0);
		
		this.p1 = owner.getPos().add(v1);
		this.p2 = owner.getPos().add(v2);
		this.p3 = owner.getPos().add(v3);
	}
	

}
