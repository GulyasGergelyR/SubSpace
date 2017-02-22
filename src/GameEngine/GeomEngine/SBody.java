package GameEngine.GeomEngine;

import org.newdawn.slick.Color;

import GameEngine.BaseEngine.SObject;

public class SBody {
	
	protected SObject owner;
	protected SHitbox hitbox;
	protected String texture;
	protected float scale;	
	protected float drawScale;
	protected Color color;
	protected float z;
	protected float mass;
	protected float transparency = 1.0f;
	
	public SBody(SObject owner, SHitbox hitbox, String texture, float scale, float drawScale){
		this.owner = owner;
		this.hitbox = hitbox;
		this.texture = texture;
		this.scale = scale;
		this.drawScale = drawScale;
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
	public float getDrawScale() {
		return drawScale;
	}
	public float getCurrentDrawScale() {
		return drawScale*scale;
	}
	public void setDrawScale(float drawScale) {
		this.drawScale = drawScale;
	}

	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public float get_Z(){
		return this.z;
	}
	
	public float getMass(){
		return mass;
	}
	
	public void setMass(float mass){
		this.mass = mass;
	}

	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}
	
}
