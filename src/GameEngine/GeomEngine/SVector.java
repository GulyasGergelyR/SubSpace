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
	// vector functions
	public SVector add(SVector v){
		return new SVector(this.x+v.x, this.y+v.y);
	}
	public SVector add(float x, float y){
		return new SVector(this.x+x, this.y+y);
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
	public SVector setLength(float length){
		if(this.l()>0.0f){
			return this.m(length/this.l());
		}
		return this;
	}
	// angle functions
	public float getAngle()
	{
		return (float) (Math.atan2(y,x)*180/Math.PI);
	}
	public SVector rotate(float f)	//rotating by 
	{
		float alpha = f*(float)Math.PI/180;
		return(new SVector((float)(Math.cos(alpha)*x+Math.sin(alpha)*y),(float)(Math.sin(alpha)*(-x)+Math.cos(alpha)*y)));
	}
}
