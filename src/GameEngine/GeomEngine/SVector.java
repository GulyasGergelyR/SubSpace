package GameEngine.GeomEngine;

import java.nio.ByteBuffer;
import java.util.Locale;


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
	public float getAbsAngleBetween(SVector v)
	{
		float alpha = Math.abs(v.getAngle()-this.getAngle());
		if(alpha>180) alpha = 360-alpha;
		return alpha;
	}
	public SVector rotate(float f)	//rotating by 
	{
		float alpha = f*(float)Math.PI/180;
		return(new SVector((float)(Math.cos(alpha)*x+Math.sin(alpha)*y),(float)(Math.sin(alpha)*(-x)+Math.cos(alpha)*y)));
	}
	
	public void addToBufferAsBigVector(ByteBuffer buffer){
		short f_x = (short)x;
		short f_y = (short)y;
		buffer.putShort(f_x);
		buffer.putShort((short)((x-f_x)*10000));
		buffer.putShort(f_y);
		buffer.putShort((short)((y-f_y)*10000));
	}
	public void addToBufferAsSmallVector(ByteBuffer buffer){
		if ((Math.abs(x) > 100)||(Math.abs(y)>100)){
			this.norm().addToBufferAsSmallVector(buffer);
			return;
		}
		short f_x = (byte)x;
		short f_y = (byte)y;
		buffer.putShort(f_x);
		buffer.putShort((short)((x-f_x)*10000));
		buffer.putShort(f_y);
		buffer.putShort((short)((y-f_y)*10000));
	}
	
	@Deprecated
	public String getString(){
		return String.format(Locale.ROOT,"%.2f;%.2f", x,y);
	}
}
