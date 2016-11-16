package WebEngine.MessageEngine;

import java.nio.ByteBuffer;

public class SM {
	protected byte[] data;
	protected byte commandId;
	protected ByteBuffer buffer;
	
	public SM(byte[] input){
		data = input;
		buffer = ByteBuffer.wrap(input);
		commandId = buffer.get();
	}

	public byte[] getData(){
		return data;
	}
	
	//Code from the Internet blame them...
	private byte[] concat(byte[] a, byte[] b) {
		   int aLen = a.length;
		   int bLen = b.length;
		   byte[] c= new byte[aLen+bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
		}
}
