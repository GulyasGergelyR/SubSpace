package GameEngine.GeomEngine;

import org.newdawn.slick.Color;

import GameEngine.BaseEngine.SObject;

public class SBody {
	
	protected SObject owner;
	protected SHitbox hitbox;
	protected String texture;
	protected float scale;	
	protected Color color;
	
	public SBody(SObject owner, SHitbox hitbox, String texture, float scale){
		this.owner = owner;
		this.hitbox = hitbox;
		this.texture = texture;
		this.scale = scale;
		this.color = new Color(255,255,255,0);
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
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
}
