package WebEngine.ComEngine;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import GameEngine.Specifications;

public abstract class SMessageParser {
	
	static String nullCh = new String(new byte[1])+"*";
	
	static String pId = "([0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12})";
	static String pCommandName = "([A-Z]{5})";
	static String pCommandHead = pId+";"+pCommandName+";";
	static SMatcher mCommand = new SMatcher(pCommandHead+".*", 0);
	static SMatcher mId = new SMatcher(pCommandHead, 1);
	static SMatcher mCommandName = new SMatcher(pCommandHead, 2);
	
	static SMatcher mConnectCommand = new SMatcher(pCommandHead+"[a-zA-Z]{0-20};"+nullCh, 0);
	static SMatcher mConnectCommandName = new SMatcher("()*"+nullCh, 1);
	
	static SMatcher mEntityCommand = new SMatcher(pCommandHead+"(([PR]M[LRM];)|([PR][WASD];))*"+nullCh, 0);
	static SMatcher mEntityCommandKeyBoard = new SMatcher("([PR][WASD]);",1);
	static SMatcher mEntityCommandMouse = new SMatcher("([PR]M[LRM]);",1);
	
	static SMatcher mPingCommand = new SMatcher(pCommandHead+"([0-9]{0,20});([0-9]{0,3});"+nullCh, 0);
	static SMatcher mPingCommandTime = new SMatcher("([0-9]{0,20});([0-9]{0,3});", 1);
	static SMatcher mPingCommandPrevPing = new SMatcher("([0-9]{0,20});([0-9]{0,3});", 2);
	
	
	//Junk
	static String test1 = "PA;PD;RD;";
	static String test2 = "PML;";
	static String test3 = "PA;PD;PW;PML;PSP;";
	static String test4 = ";13321312566;999;3;";
	
	static String pattern4 = "\\d{0,20};\\d{0,3};"+nullCh;
	static String pattern5 = "([PR][WASD];)*"+nullCh;
	static String pattern6 = "([PR][WASD];)*"+nullCh;
	static String pattern1 = "([PR][WASD];)*"+nullCh;
	static String pattern2 = "([PR]M[LR];)*"+nullCh;
	
	//IsValid
	public static boolean IsMessageValid(SMessage message){
		String input = message.getMessageString();
		if (mCommand.matches(input)){
			String command = mCommandName.getMatch(input);
			if (Specifications.EntityCommands.contains(command))
				return IsEntityMessageValid(message);
			if (Specifications.PingCommands.contains(command))
				return IsPingMessageValid(message);
			if(command.equals("CNNCL"))
				return IsConnectMessageValid(message);
			return false;
		}
		else return false;
	}
	public static boolean IsEntityMessageValid(SMessage message){
		return mEntityCommand.matches(message.getMessageString());
	}
	public static boolean IsPingMessageValid(SMessage message){
		return mPingCommand.matches(message.getMessageString());
	}
	public static boolean IsConnectMessageValid(SMessage message){
		return mConnectCommand.matches(message.getMessageString());
	}
	/// Common
	public static String getCommand(SMessage message){
		return mCommandName.getMatch(message.getMessageString());
	}
	
	public static String getId(SMessage message){
		return mId.getMatch(message.getMessageString());
	}
	// Connect Message
	
	// Entity Message
	public static LinkedList<String> getEntityCommandKeyBoard(SMessage message){
		return mEntityCommandKeyBoard.getMatches(message.getMessageString());
	}
	public static LinkedList<String> getEntityCommandMouse(SMessage message){
		return mEntityCommandMouse.getMatches(message.getMessageString());
	}
	// Ping Message
	public static String getPingCommandTime(SMessage message){
		return mPingCommandTime.getMatch(message.getMessageString());
	}
	public static String getPingCommandPrevPing(SMessage message){
		return mPingCommandPrevPing.getMatch(message.getMessageString());
	}
	
	
	/////////////////////////////////////////////
	private static class SMatcher{
		Matcher matcher;
		Pattern pattern;
		int group;
		public SMatcher(String pattern, int group){
			this.pattern = Pattern.compile(pattern);
			this.group = group;
		}
		private void Match(String S){
			matcher = pattern.matcher(S);
		}
		public LinkedList<String> getMatches(String S){
			Match(S);
			LinkedList<String> matches = new LinkedList<String>();
			while(matcher.find()){
				matches.add(matcher.group(group));
			}
			return matches;
		}
		public String getMatch(String S){
			Match(S);
			if(matcher.find())
				return matcher.group(group);
			else
				return null;
		}
		public boolean matches(String S){
			Match(S);
			return matcher.matches();
		}
	}
}
