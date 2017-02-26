package WebEngine.MessageEngine;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import GameEngine.Specifications;

public class SM {
	protected byte[] data;
	protected byte commandId;
	protected ByteBuffer buffer;
	protected InetAddress address;
	protected int port;
	
	public SM(DatagramPacket receivePacket){
		data = new byte[Specifications.DataLength];
		System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());
		address = receivePacket.getAddress();
		port = receivePacket.getPort();
		buffer = ByteBuffer.wrap(data);
		commandId = buffer.get();
	}
	public SM(){
		data = new byte[Specifications.DataLength];
		buffer = ByteBuffer.wrap(data);
	}
	public byte[] getData(){
		int dataLength = 0; 
		for(int i=data.length-1;i>0;i--)
			if (data[i] != 0){
				dataLength = i+1;
				break;
			}
		byte[] temp = new byte[dataLength];
		System.arraycopy(data, 0, temp, 0, dataLength);
		return temp;
	}
	public ByteBuffer getBuffer(){
		return buffer;
	}
	public byte getCommandId(){
		return commandId;
	}
	public void setCommandId(byte commandId){
		this.commandId = commandId;
		int pos = this.buffer.position();
		this.buffer.put(0, this.commandId);
		this.buffer.position(pos);
	}
	public boolean isValid(){
		return true;
		//TODO implement CRC
	}
	public InetAddress getAddress() {
		return address;
	}
	public int getPort() {
		return port;
	}
	
}
