package RenderingEngine;

import org.newdawn.slick.opengl.Texture;

import GameEngine.GeomEngine.SVector;

public class SRenderObject {
	Texture texture;
	SVector v;
	float rotateBy;
	float scale;
	float transparency;
	
	public SRenderObject(Texture texture,SVector v){
		this.texture = texture;
		this.v = v;
		this.rotateBy = 0.0f;
		this.scale = 1.0f;
		this.transparency = 1.0f;
	}
	
	public SRenderObject(Texture texture,SVector v, float rotateBy){
		this.texture = texture;
		this.v = v;
		this.rotateBy = rotateBy;
		this.scale = 1.0f;
		this.transparency = 1.0f;
	}
	
	public SRenderObject(Texture texture,SVector v,float rotateBy,float scale,float transparency){
		this.texture = texture;
		this.v = v;
		this.rotateBy = rotateBy;
		this.scale = scale;
		this.transparency = transparency;
	}
	
}
