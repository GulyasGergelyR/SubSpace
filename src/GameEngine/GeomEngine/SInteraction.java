package GameEngine.GeomEngine;

import GameEngine.Specifications;
import GameEngine.BaseEngine.SMobile;
import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.EntityEngine.SHitable;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.DebrisEngine.SAsteroid;
import GameEngine.ObjectEngine.DebrisEngine.SDebris;
import GameEngine.ObjectEngine.EffectEngine.SEffectFactory;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUp;
import GameEngine.WeaponEngine.SBullet;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SInteraction {
	private SMobile mobile1;
	private SMobile mobile2;
	private float t = 1f;
	private float cv1;
	private float cv2;
	private SVector contactPoint;
	private boolean collisionExplosion = false;
	private boolean happened;
	
	public SInteraction(SMobile mobile1, SMobile mobile2){
		this.mobile1 = mobile1;
		this.mobile2 = mobile2;
		
		if (intersects()){
			if ((mobile1 instanceof SPowerUp || mobile2 instanceof SPowerUp) &&
					(mobile1 instanceof SEntity || mobile2 instanceof SEntity)){
				SPowerUp powerUp = null;
				SEntity entity = null;
				if (mobile1 instanceof SPowerUp)
					powerUp = (SPowerUp) mobile1;
				else
					powerUp = (SPowerUp) mobile2;
				if (mobile2 instanceof SEntity)
					entity = (SEntity) mobile2;
				else
					entity = (SEntity) mobile1;
				
				if (!powerUp.applyToEntity(entity))
					return;
				powerUp.setObjectState(ObjectState.WaitingDelete);
				SM message = SMPatterns.getObjectDeleteMessage(powerUp);
				SMain.getCommunicationHandler().SendMessage(message);
				SFH.PowerUps.powerUpApplied(powerUp.getType());
				happened = true;
			} else {
				if (collide()){
					happened = true;
					if (mobile1 instanceof SHitable || mobile2 instanceof SHitable){
						if ((mobile1 instanceof SDebris || mobile2 instanceof SDebris)||
								(mobile1 instanceof SEntity && mobile2 instanceof SEntity)){
							if (getRelativeSpeed() > 5)
								collisionExplosion = true;
						}
						else {
							//collisionExplosion = true;
						}
						applyDamage(mobile1, mobile2);
						applyDamage(mobile2, mobile1);
						
						if (collisionExplosion){
							SM explosionMessage = SMPatterns.getAnimationObjectCreateMessage(contactPoint, (byte)61);
							SMain.getCommunicationHandler().SendMessage(explosionMessage);
						}
					}
				}
			}
		}
	}
	
	private boolean intersects(){
		SHitboxSpherical hitbox1 = (SHitboxSpherical)mobile1.getBody().getHitbox();
		SHitboxSpherical hitbox2 = (SHitboxSpherical)mobile2.getBody().getHitbox();
		
		SVector v1 = mobile1.getPos().sub(mobile1.getPrevPos());
		SVector v2 = mobile2.getPos().sub(mobile2.getPrevPos());
		float ax = mobile1.getPrevPos().getX()-mobile2.getPrevPos().getX();
		float ay = mobile1.getPrevPos().getY()-mobile2.getPrevPos().getY();
		float bx = v1.getX() - v2.getX();
		float by = v1.getY() - v2.getY();
		//System.out.println();
		//System.out.println("ax "+ax+" bx "+bx+"ay "+ay+" by "+by);
		float a = sqr(bx) + sqr(by);
		float b = 2*ax*bx + 2*ay*by;
		float c = sqr(ax) + sqr(ay) - sqr(hitbox1.getRadius() + hitbox2.getRadius());
		//System.out.println("a "+a+" b "+b+" c "+c);
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
		//System.out.println("t "+t);
		float d = sqr(ax+t*bx)+sqr(ay+t*by);
		//System.out.println("d "+Math.sqrt(d)+" < "+sqr(hitbox1.getRadius()+hitbox2.getRadius()));
		if (d<=sqr(hitbox1.getRadius()+hitbox2.getRadius())){
			return true;
		} else {
			return false;
		}
	}
	
	private boolean collide(){
		SVector pos1 = mobile1.getPrevPos().add(mobile1.getPos().sub(mobile1.getPrevPos()).m(t));
		SVector pos2 = mobile2.getPrevPos().add(mobile2.getPos().sub(mobile2.getPrevPos()).m(t));
		// Determine contact point
		contactPoint = pos1.add(pos2.sub(pos1).setLength(
				((SHitboxSpherical)mobile1.getBody().getHitbox()).getRadius()));
		
		if ((mobile1 instanceof SEntity && mobile2 instanceof SBullet)||
				(mobile2 instanceof SEntity && mobile1 instanceof SBullet)){
					return true;
				}
		// TODO add moveDir interpolation
		
		SVector n = pos2.sub(pos1);
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
		mobile1.modifyCurrentPos(pos1.add(mobile1.getMoveDir().m((1-t)*SMain.getDeltaRatio())));
		SVector v2delta = n.setLength(-cv2+cV2);
		mobile2.setMoveDir(mobile2.getMoveDir().add(v2delta));
		mobile2.modifyCurrentPos(pos2.add(mobile2.getMoveDir().m((1-t)*SMain.getDeltaRatio())));
		return true;
	}
	
	private void applyDamage(SMobile source, SMobile target){
		if (target instanceof SHitable){
			if (source instanceof SBullet){
				if (((SHitable)target).gotHit(((SBullet)source).getDamage(), source))
					((SBullet)source).getOwner().getPlayer().addKill(1);
			}
			else if (source instanceof SAsteroid){
				((SHitable)target).gotHit(getRelativeSpeed()/3, source);
			}
			else if (source instanceof SEntity){
				float damage = getRelativeSpeed()/3;
				if (((SEntity)source).underEffect(SEffectFactory.EffectBull)){
						damage = getRelativeSpeed() + 10;
				}
				((SHitable)target).gotHit(damage, source);
				
				if (!((SEntity)source).isAlive() || !((SEntity)target).isAlive()){
					collisionExplosion = false;
				}
			}
		}
	}
	
	public float getRelativeSpeed(){
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
