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
	
	public SM(byte[] input){
		data = input;
		buffer = ByteBuffer.wrap(data);
		commandId = buffer.get();
	}
	public SM(DatagramPacket receivePacket){
		data = receivePacket.getData();
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
