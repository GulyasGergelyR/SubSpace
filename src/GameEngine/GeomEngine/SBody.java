package GameEngine.GeomEngine;

import GameEngine.BaseEngine.SObject;

public class SBody {
	
	protected SObject owner;
	protected SHitbox hitbox;
	protected String texture;
	protected float scale;	
	
	public SBody(SObject owner, SHitbox hitbox, String texture, float scale){
		this.owner = owner;
		this.hitbox = hitbox;
		this.texture = texture;
		this.scale = scale;
	}
		
	public SHitbox getHitbox() {
		return hitbox;
	}
	public void setHitbox(SHitbox hitbox) {
		this.hitbox = hitbox;
	}
	public String getTexture() {
		return texture;
	}
	public void setTexture(String texture) {
		this.texture = texture;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}

}
