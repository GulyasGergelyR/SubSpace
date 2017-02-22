package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;

public class SControl {
	protected int sendCounter = 0;
	protected int maxSendCounter = 60;
	protected SMobile Owner;
	
	public SControl(SMobile mobile){
		Owner = mobile;
	}
	public  boolean setKeyTo(int key, boolean state){return false;}
	public boolean setMouseTo(int key, boolean state){return false;}
	protected void Think(){}
	protected void Act(){}
	public void ThinkAndAct(){}
	
	public int getSendCounter() {
		return sendCounter;
	}
	public void setSendCounter(int sendCounter) {
		this.sendCounter = sendCounter;
	}
	public int getMaxSendCounter() {
		return maxSendCounter;
	}
	public void setMaxSendCounter(int maxSendCounter) {
		this.maxSendCounter = maxSendCounter;
	}
	
	
}
