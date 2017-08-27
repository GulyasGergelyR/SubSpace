package GameEngine.ControlEngine;

import GameEngine.BaseEngine.SMobile;

public class SControl<T extends SMobile> {
	protected int sendCounter = 0;
	protected int maxSendCounter = 60;
	protected T Owner;
	
	public SControl(T mobile){
		Owner = mobile;
	}
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
