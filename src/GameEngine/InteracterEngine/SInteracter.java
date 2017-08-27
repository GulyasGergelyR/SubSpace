package GameEngine.InteracterEngine;

import GameEngine.BaseEngine.SMobile;
import GameEngine.GeomEngine.SHitboxSpherical;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public class SInteracter<Type1 extends SMobile, Type2 extends SMobile>{
	private SMobile mobile1;
	private SMobile mobile2;
	
	SHitboxSpherical hitbox1;
	SHitboxSpherical hitbox2;
	
	private float t = 1f;
	private float cv1;
	private float cv2;
	
	private float m1;	// For entity interactions
	private float m2;
	
	private SVector contactPoint;  // point of contact
	private SVector posDuringContact1;  // center points during contact
	private SVector posDuringContact2;
	
	private boolean happened;
	
	public SInteracter(Type1 mobile1, Type2 mobile2){
		this.mobile1 = mobile1;
		this.mobile2 = mobile2;
		
		hitboxSelector();
		
		if (intersects()){
			setMass();
			if (collide()){
				happened = true;
				collisionHook();
			}
		}
	}
	
	private boolean intersects(){ 
		SVector v1 = mobile1.getPos().sub(mobile1.getPrevPos());
		SVector v2 = mobile2.getPos().sub(mobile2.getPrevPos());
		float ax = mobile1.getPrevPos().getX()-mobile2.getPrevPos().getX();
		float ay = mobile1.getPrevPos().getY()-mobile2.getPrevPos().getY();
		float bx = v1.getX() - v2.getX();
		float by = v1.getY() - v2.getY();
		float a = sqr(bx) + sqr(by);
		float b = 2*ax*bx + 2*ay*by;
		float c = sqr(ax) + sqr(ay) - sqr(hitbox1.getRadius() + hitbox2.getRadius());
		if (a < 0.0001f){
			if (Math.abs(b) < 0.0001f){
				t = 1;
			} else {
				t = -c/b;
			}
		}else{
			float det = (float) Math.sqrt(sqr(b) - 4*a*c);
			float t1 = (-b+det)/(2*a);
			float t2 = (-b-det)/(2*a);
			float min = 1;
			if (t1 < min && t1 > 0){
				min = t1;
			}
			if (t2 < min && t2 > 0){
				min = t2;
			}
			t = min;
		}
		
		if (t > 1){
			t = 1;
		} else if (t < 0){
			t = 0;
		}
		if (t < 1 && t > 0){
			return true;
		}
		float d = sqr(ax+t*bx)+sqr(ay+t*by);
		if (d<=sqr(hitbox1.getRadius()+hitbox2.getRadius())){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean collide(){
		calcContactPoint();
		return bounce();
	}
	
	private void hitboxSelector(){
		hitbox1 = (SHitboxSpherical)mobile1.getBody().getHitbox();
		hitbox2 = (SHitboxSpherical)mobile2.getBody().getHitbox();		
	}
	
	private void collisionHook(){
		// for explosion, applying damage
	}
	
	private void calcContactPoint(){
		posDuringContact1 = mobile1.getPrevPos().add(mobile1.getPos().sub(mobile1.getPrevPos()).m(t));
		posDuringContact2 = mobile2.getPrevPos().add(mobile2.getPos().sub(mobile2.getPrevPos()).m(t));
		// Determine contact point
		contactPoint = posDuringContact1.add(posDuringContact2.sub(posDuringContact1).setLength(hitbox1.getRadius()));
	}
	
	private boolean bounce(){
		SVector n = posDuringContact2.sub(posDuringContact1);
		cv1 = n.getProjection(mobile1.getMoveDir());
		cv2 = n.getProjection(mobile2.getMoveDir());
		
		//This could be checked in a smaller form, but this way its easier to understand
		if (cv1 < 0 && cv2 > 0)
			return false;	// they are going away from each other
		if (cv1>0 && cv1 < cv2)
			return false;
		if (cv1<0 && cv1 < cv2)
			return false;
		float k = 1.0f;
		float vs = (m1*cv1+m2*cv2)/(m1+m2);
		float cV1 = vs - k*(cv1-vs);
		float cV2 = vs + k*(vs-cv2);
		SVector v1delta = n.setLength(-cv1+cV1);
		mobile1.setMoveDir(mobile1.getMoveDir().add(v1delta));
		mobile1.modifyCurrentPos(posDuringContact1.add(mobile1.getMoveDir().m((1-t)*SMain.getDeltaRatio())));
		SVector v2delta = n.setLength(-cv2+cV2);
		mobile2.setMoveDir(mobile2.getMoveDir().add(v2delta));
		mobile2.modifyCurrentPos(posDuringContact2.add(mobile2.getMoveDir().m((1-t)*SMain.getDeltaRatio())));
		return true;
	}
	
	private void setMass(){
		m1 = mobile1.getBody().getMass();
		m2 = mobile2.getBody().getMass();
	}
	
	protected float getRelativeSpeed(){
		return Math.abs(cv1-cv2);
	}
	
	public boolean IsHappened(){
		return happened;
	}
	
	private float sqr(float f){
		return f*f;
	}

	public SVector getContactPoint() {
		return contactPoint;
	}
}
