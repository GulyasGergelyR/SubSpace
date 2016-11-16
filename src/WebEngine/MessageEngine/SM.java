package WebEngine.MessageEngine;

import java.nio.ByteBuffer;

import GameEngine.Specifications;

public class SM {
	protected byte[] data;
	protected byte commandId;
	protected ByteBuffer buffer;
	
	public SM(byte[] input){
		data = input;
		buffer = ByteBuffer.wrap(data);
		commandId = buffer.get();
	}
	public SM(){
		data = new byte[Specifications.DataLength];
		buffer = ByteBuffer.wrap(data);
	}
	public void add(byte b){
		buffer.put(b);
	}
	public void add(byte[] b){
		buffer.put(b);
	}
	public byte[] getData(){
		return data;
	}
	public ByteBuffer getBuffer(){
		return buffer;
	}
	public byte getCommandId(){
		return commandId;
	}
	public boolean isValid(){
		return true;
		//TODO implement CRC
	}
}
