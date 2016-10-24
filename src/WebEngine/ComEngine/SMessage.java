package WebEngine.ComEngine;

import java.util.Arrays;
import java.util.UUID;

import GameEngine.Specifications;

public class SMessage {
	protected byte[] rawData;
	protected String messageString;
	protected UUID Id;
	protected String commandName;
	protected String content;
	protected boolean valid;
	
	public SMessage(byte[] input){
		this.rawData = input;
		this.messageString = new String(input);
		if (SMessageParser.IsMessageValid(input)){
			this.Id = UUID.fromString(SMessageParser.getId(this));
			this.commandName = SMessageParser.getCommand(this);
			this.content = SMessageParser.getContent(this); //not really used, parser has its own functions
			valid = true;
		}
	}
	
	public SMessage(UUID Id, String commandName, String content){
		this.Id = Id;
		this.commandName = commandName;
		this.content = content;
		this.rawData = createRawData();
		this.messageString = new String(rawData);
	}

	public byte[] getRawData(){
		return rawData;
	}
	public String getMessageString(){
		return messageString;
	}
	
	public UUID getId() {
		return Id;
	}
	public boolean isValid(){
		return valid;
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
	
	public void addContent(String s){
		this.content += s +";";
	}
	public byte[] createRawData(){
		byte[] temp = concat((Id.toString()+";").getBytes(),(commandName+";").getBytes());
		temp = concat(temp, content.getBytes());
		if(temp.length<Specifications.DataLength){
			temp = concat(temp, new byte[Specifications.DataLength-temp.length]);
		}
		return temp;
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
