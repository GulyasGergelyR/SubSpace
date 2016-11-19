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
	
	@Deprecated
	public SM(byte[] input){
		data = new byte[Specifications.DataLength];
		for(int i=0;i<data.length;i++)
			data[i] = input[i];
		buffer = ByteBuffer.wrap(data);
		commandId = buffer.get();
	}
	public SM(DatagramPacket receivePacket){
		byte[] input = receivePacket.getData();
		data = new byte[Specifications.DataLength];
		for(int i=0;i<data.length;i++)
			data[i] = input[i];
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
	public InetAddress getAddress() {
		return address;
	}
	public int getPort() {
		return port;
	}
	
}
