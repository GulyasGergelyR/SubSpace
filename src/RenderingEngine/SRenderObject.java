package RenderingEngine;

import java.awt.Color;

import org.newdawn.slick.opengl.Texture;

import GameEngine.SResLoader;
import GameEngine.GeomEngine.SVector;

public class SRenderObject {
	Texture texture;
	SVector v;
	float rotateBy;
	float scale;
	float transparency;
	Color color;
	
	public SRenderObject(String texture,SVector v){
		this.texture = SResLoader.getTexture(texture);
		this.v = v;
		this.rotateBy = 0.0f;
		this.scale = 1.0f;
		this.transparency = 1.0f;
		int alpha = (int)(transparency*255);
		this.color = new Color(255,255,255,alpha);
	}
	
	public SRenderObject(String texture,SVector v, float rotateBy){
		this.texture = SResLoader.getTexture(texture);
		this.v = v;
		this.rotateBy = rotateBy;
		this.scale = 1.0f;
		this.transparency = 1.0f;
		int alpha = (int)(transparency*255);
		this.color = new Color(255,255,255,alpha);
	}
	
	public SRenderObject(String texture,SVector v,float rotateBy,float scale,float transparency){
		this.texture = SResLoader.getTexture(texture);
		this.v = v;
		this.rotateBy = rotateBy;
		this.scale = scale;
		this.transparency = transparency;
	}
	
}
