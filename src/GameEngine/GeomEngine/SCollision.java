package GameEngine.GeomEngine;

import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;
import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SHitable;
import GameEngine.ObjectEngine.DebrisEngine.SAsteroid;
import GameEngine.ObjectEngine.EffectEngine.SEffectFactory;

public class SCollision {
	private SMobile mobile1;
	private SMobile mobile2;
	private float cv1;
	private float cv2;
	
	private boolean happened = false;
	
	private boolean mobile1Hit = false;
	private boolean mobile2Hit = false;
	
	public SCollision(SMobile mobile1, SMobile mobile2){
		this.mobile1 = mobile1;
		this.mobile2 = mobile2;
		happened = collide();
		if (happened){
			applyDamage(mobile2, mobile1);
			applyDamage(mobile1, mobile2);
		}
	}
	
	private void applyDamage(SMobile source, SMobile target){
		if (target instanceof SHitable){
			if (source instanceof SAsteroid){
				((SHitable)target).gotHit(getRelativeSpeed()/3, source);
			}
			if (source instanceof SEntity){
				float damage = getRelativeSpeed()/3;
				if (((SEntity)source).underEffect(SEffectFactory.EffectBull)){
						damage = getRelativeSpeed() + 10;
				}
				((SHitable)target).gotHit(damage, source);
			}
		}
		
		
	}
	
	private boolean collide(){
		SVector n = mobile2.getPos().sub(mobile1.getPos());
		cv1 = n.getProjection(mobile1.getMoveDir());
		cv2 = n.getProjection(mobile2.getMoveDir());
		
		//This could be checked in a smaller form, but this way its easier to understand
		if (cv1 < 0 && cv2 > 0)
			return false;	// they are going away from eachother
		if (cv1>0 && cv1 < cv2)
			return false;
		if (cv1<0 && cv1 < cv2)
			return false;
		
		float m1;
		float m2;
		if (mobile1 instanceof SEntity && mobile2 instanceof SEntity){
			m1 = Specifications.entityMass;
			m2 = Specifications.entityMass;
		} else {
			m1 = mobile1.getBody().getMass();
			m2 = mobile2.getBody().getMass();
		}
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
	
	public boolean IsHappened(){
		return happened;
	}
	
	@Deprecated
	public boolean IsMobile1Hit(){
		return mobile1Hit;
	}
	@Deprecated
	public boolean IsMobile2Hit(){
		return mobile2Hit;
	}
	
	public float getRelativeSpeed(){
		return Math.abs(cv1-cv2);
	}
}
