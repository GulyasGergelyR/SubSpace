package WebEngine.ComEngine;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SMessageParser {
	
	static String nullCh = new String(new byte[1])+"*";
	
	static String pId = "([0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12});";
	static String pVector = "(-?[0-9]{1,4}.[0-9]{1,2});";
	static String pCommandName = "([A-Z]{5});";
	static String pCommandHead = pId+pCommandName;
	
	static SMatcher mCommand = new SMatcher(pCommandHead+".*", 0);
	static SMatcher mId = new SMatcher(pCommandHead, 1);
	static SMatcher mCommandName = new SMatcher(pCommandHead, 2);
	static SMatcher mContent = new SMatcher(pCommandHead+"([^"+new String(new byte[1])+"]*)", 3);
	
	//static SMatcher mConnectCommand = new SMatcher(pCommandHead+"([a-zA-Z]{5,20});"+nullCh, 0);
	static SMatcher mConnectCommandName = new SMatcher(pCommandHead+"([a-zA-Z]{5,20});"+nullCh, 3);
	
	//static SMatcher mEntityCommand = new SMatcher(pCommandHead+"(([PR]M[LRM];)|([PR][WASD];)|(K[0-9];))*"+nullCh, 0);
	static SMatcher mEntityCommandWASD = new SMatcher(";([PR][WASD]);",1);
	static SMatcher mEntityCommandNums = new SMatcher(";(K[0-9]);",1);
	static SMatcher mEntityCommandMouse = new SMatcher(";([PR]M[LRM]);",1);
	
	static SMatcher mPosX = new SMatcher(";p;"+pVector+pVector,1);
	static SMatcher mPosY = new SMatcher(";p;"+pVector+pVector,2);
	static SMatcher mLookDirX = new SMatcher(";ld;"+pVector+pVector,1);
	static SMatcher mLookDirY = new SMatcher(";ld;"+pVector+pVector,2);
	static SMatcher mMoveDirX = new SMatcher(";md;"+pVector+pVector,1);
	static SMatcher mMoveDirY = new SMatcher(";md;"+pVector+pVector,2);
	static SMatcher mAcclDirX = new SMatcher(";ad;"+pVector+pVector,1);
	static SMatcher mAcclDirY = new SMatcher(";ad;"+pVector+pVector,2);
	
	//static SMatcher mPingCommand = new SMatcher(pCommandHead+"([0-9]{1,20});([0-9]{0,3});?"+nullCh, 0);
	static SMatcher mPingCommandTime = new SMatcher(";([0-9]{1,20});([0-9]{0,3});?", 1);
	static SMatcher mPingCommandPrevPing = new SMatcher(";([0-9]{1,20});([0-9]{1,3});", 2);
	
	//IsValid
	public static boolean IsMessageValid(byte[] input){
		if (mCommand.matches(new String(input))){
			return true;
		}
		else {
			System.out.println("Invalid message");
			return false;
		}
	}
	public static boolean IsMessageValid(SMessage message){
		if (mCommand.matches(message.getMessageString())){
			return true;
		}
		else {
			System.out.println("Invalid message");
			return false;
		}
	}
	/// Common
	public static String getCommand(SMessage message){
		return mCommandName.getMatch(message.getMessageString());
	}
	public static String getId(SMessage message){
		return mId.getMatch(message.getMessageString());
	}
	public static String getContent(SMessage message){
		return mContent.getMatch(message.getMessageString());
	}
	// Connect Message
	public static String getConnectCommandName(SMessage message){
		return mConnectCommandName.getMatch(message.getMessageString());
	}
	
	// Entity Message
	public static LinkedList<String> getEntityCommandWASD(SMessage message){
		return mEntityCommandWASD.getMatches(message.getMessageString());
	}
	public static LinkedList<String> getEntityCommandNums(SMessage message){
		return mEntityCommandNums.getMatches(message.getMessageString());
	}
	public static LinkedList<String> getEntityCommandMouse(SMessage message){
		return mEntityCommandMouse.getMatches(message.getMessageString());
	}
	// Obj Message
	public static String getPosX(SMessage message){
		return mPosX.getMatch(message.getMessageString());
	}
	public static String getPosY(SMessage message){
		return mPosY.getMatch(message.getMessageString());
	}
	public static String getLookDirX(SMessage message){
		return mLookDirX.getMatch(message.getMessageString());
	}
	public static String getLookDirY(SMessage message){
		return mLookDirY.getMatch(message.getMessageString());
	}
	public static String getMoveDirX(SMessage message){
		return mMoveDirX.getMatch(message.getMessageString());
	}
	public static String getMoveDirY(SMessage message){
		return mMoveDirY.getMatch(message.getMessageString());
	}
	public static String getAcclDirX(SMessage message){
		return mAcclDirX.getMatch(message.getMessageString());
	}
	public static String getAcclDirY(SMessage message){
		return mAcclDirY.getMatch(message.getMessageString());
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
