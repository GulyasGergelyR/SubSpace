package WebEngine;

import java.util.Arrays;
import java.util.UUID;

import GameEngine.Specifications;

public class SMessage {
	protected UUID Id;
	protected String commandName;
	protected String content;
	protected boolean Invalid;
	
	public SMessage(byte[] input){
		String uuid = new String(Arrays.copyOfRange(input, 0,36));
		if (uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
			this.Id = UUID.fromString(uuid);
		}else
			this.Invalid = true;
		this.commandName = new String(Arrays.copyOfRange(input, 36,41));
		this.content = new String(Arrays.copyOfRange(input, 41,Specifications.DataLength));
	}
	
	public SMessage(UUID Id, String commandName, String content){
		this.Id = Id;
		this.commandName = commandName;
		this.content = content;
	}

	public UUID getId() {
		return Id;
	}
	public boolean isValid(){
		return !this.Invalid;
	}
	public String getCommandName() {
		return commandName;
	}
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String getContent() {
		return content;
	}
	public byte[] getData(){
		byte[] temp = concat(Id.toString().getBytes(),commandName.getBytes());
		temp = concat(temp, content.getBytes());
		if(temp.length<Specifications.DataLength){
			temp = concat(temp, new byte[Specifications.DataLength-temp.length]);
		}
		return temp;
	}
	
	private byte[] concat(byte[] a, byte[] b) {
		   int aLen = a.length;
		   int bLen = b.length;
		   byte[] c= new byte[aLen+bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
		}
}
