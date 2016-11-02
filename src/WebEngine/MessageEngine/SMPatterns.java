package WebEngine.MessageEngine;

import java.nio.ByteBuffer;

import GameEngine.GeomEngine.SVector;

public class SMPatterns {
	//Message Patterns
	//M is mask, C is command
	
	//0111 xxxx Connect commands
	public static byte MConnect = 0x70;
	public static byte CConnect = 0x71;
	public static byte CDisconnect = 0x7F;
	public static byte CConnectAllowed = 0x73;
	public static byte CConnectNotAllowed = 0x74;
	
	//0100 xxxx Ping commands
	public static byte MPing = 0x40;
	public static byte CPingRequest = 0x41;
	public static byte CPingAnswer = 0x42;
	
	//0001 xxxx Game commands
	public static byte MGameComamnd = 0x10;
	
	//0001 1xxx Entity commands
	public static byte MEntity = 0x14;
	public static byte CEntityCreate = 0x19;
	public static byte CEntityUpdate = 0x1A;
	public static byte CEntityDelete = 0x1F;
	
	//0001 0xxx Object commands
	public static byte MObject = 0x10;
	public static byte CObjectCreate = 0x11;
	public static byte CObjectUpdate = 0x12;
	public static byte CObjectDelete = 0x1F;
	
	public static int getId(ByteBuffer buffer){ // 2 byte long
		return buffer.getChar();
	}
	public static SVector getBigVector(ByteBuffer buffer){ //[+-32768],[9999] - 4 byte long
		float x =  buffer.getChar()+ buffer.getChar()/1000f-32768;
		float y =  buffer.getChar()+ buffer.getChar()/1000f-32768;
		return new SVector(x,y);
	}
	public static SVector getSmallVector(ByteBuffer buffer){ //[255],[9999] - 3 byte long
		float x =  buffer.get()+ buffer.getChar()/1000f;
		float y =  buffer.get()+ buffer.getChar()/1000f;
		return new SVector(x,y);
	}
	
	
}
