package WebEngine;

import java.util.Arrays;
import java.util.UUID;

import GameEngine.Specifications;

public class SMessage {
	protected UUID Id;
	protected String command;
	protected String content;
	
	public SMessage(byte[] input){
		this.Id = UUID.fromString(new String(Arrays.copyOfRange(input, 0,16)));
		this.command = new String(Arrays.copyOfRange(input, 17,21));
		this.content = new String(Arrays.copyOfRange(input, 22,Specifications.DataLength));
	}
	
	public SMessage(UUID Id, String command, String content){
		this.Id = Id;
		this.command = command;
		this.content = content;
	}

	public UUID getId() {
		return Id;
	}

	public String getCommand() {
		return command;
	}

	public String getContent() {
		return content;
	}
	
}
