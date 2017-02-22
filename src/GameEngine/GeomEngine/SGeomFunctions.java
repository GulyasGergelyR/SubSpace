package GameEngine.GeomEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SObject;

public class SGeomFunctions {

	public static boolean intersects(SObject object1, SObject object2){
		SHitbox hitbox1 = object1.getBody().getHitbox();
		SHitbox hitbox2 = object2.getBody().getHitbox();
		if (hitbox1 instanceof SHitboxSpherical || hitbox2 instanceof SHitboxSpherical){
			if (hitbox1 instanceof SHitboxSpherical && hitbox2 instanceof SHitboxSpherical){
				float d = object1.getPos().d(object2.getPos());
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
	
	public static boolean collide(SMobile mobile1, SMobile mobile2){
		SVector n = mobile2.getPos().sub(mobile1.getPos());
		float cv1 = n.getProjection(mobile1.getMoveDir());
		float cv2 = n.getProjection(mobile2.getMoveDir());
		
		if (cv1 < 0 && cv2 > 0)
			return false;
		
		float m1 = mobile1.getBody().getMass();
		float m2 = mobile2.getBody().getMass();
		/*
		float imp1 = m1 * cv1;
		float imp2 = m2 * cv2;
		float en1 = 0.5f * imp1 * cv1;
		float en2 = 0.5f * imp2 * cv2;*/
		float k = 1.0f;
		float vs = (m1*cv1+m2*cv2)/(m1+m2);
		float cV1 = vs - k*(cv1-vs);
		float cV2 = vs + k*(vs-cv2);
		SVector v1delta = n.setLength(-cv1+cV1);
		mobile1.setMoveDir(mobile1.getMoveDir().add(v1delta));
		SVector v2delta = n.setLength(-cv2+cV2);
		mobile2.setMoveDir(mobile2.getMoveDir().add(v2delta));
		return true;
	}
}
