package GameEngine.GeomEngine;

import GameEngine.BaseEngine.SObject;

public class SGeomFunctions {

	
	public static boolean intersects(SObject object1, SObject object2){
		SHitbox hitbox1 = object1.getBody().getHitbox();
		SHitbox hitbox2 = object2.getBody().getHitbox();
		if (hitbox1 instanceof SHitboxSpherical || hitbox2 instanceof SHitboxSpherical){
			if (hitbox1 instanceof SHitboxSpherical && hitbox2 instanceof SHitboxSpherical){
				if (object1.getPos().d(object2.getPos()) <=
					((SHitboxSpherical)hitbox1).getRadius()+((SHitboxSpherical)hitbox2).getRadius()){
					return true;
				}
				else{
					return false;
				}
				
			}
		}
		else if (hitbox1 instanceof SHitboxTriangular || hitbox2 instanceof SHitboxTriangular){
			if (hitbox1 instanceof SHitboxTriangular && hitbox2 instanceof SHitboxTriangular){
				
				/*
				function ptInTriangle(p, p0, p1, p2) {
				    var dX = p.x-p2.x;
				    var dY = p.y-p2.y;
				    var dX21 = p2.x-p1.x;
				    var dY12 = p1.y-p2.y;
				    var D = dY12*(p0.x-p2.x) + dX21*(p0.y-p2.y);
				    var s = dY12*dX + dX21*dY;
				    var t = (p2.y-p0.y)*dX + (p0.x-p2.x)*dY;
				    if (D<0) return s<=0 && t<=0 && s+t>=D;
				    return s>=0 && t>=0 && s+t<=D;
				}*/
			}
		}
		return false;
	}
}
