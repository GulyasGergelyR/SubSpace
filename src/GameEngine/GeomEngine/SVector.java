package GameEngine.GeomEngine;

public class SVector {
	private float x;
	private float y;
	
	// initialize
	public SVector(){
		this.x = 0.0f;
		this.y = 0.0f;
	}
	public SVector(float x, float y){
		this.x = x;
		this.y = y;
	}
	public SVector(SVector v){
		this.x = v.x;
		this.y = v.y;
	}
	// get - set
	public float getX(){
		return this.x;
	}
	public float getY(){
		return this.y;
	}
	public void setX(float x){
		this.x = x;
	}
	public void setY(float y){
		this.y = y;
	}
	// functions
	public SVector add(SVector v){
		return new SVector(this.x+v.x, this.y+v.y);
	}
	public SVector sub(SVector v){
		return new SVector(this.x-v.x, this.y-v.y);
	}
	public SVector m(float f){
		return new SVector(this.x*f, this.y*f);
	}
	public SVector norm(){
		return new SVector(this.x/this.l(), this.y/this.l());
	}
	public float l(){
		return (float) Math.sqrt(this.x*this.x+this.y*this.y);
	}
}
