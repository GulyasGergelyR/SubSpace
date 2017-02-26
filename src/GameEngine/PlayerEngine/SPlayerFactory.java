package GameEngine.PlayerEngine;

import GameEngine.ObjectEngine.SFactory;

public class SPlayerFactory extends SFactory<SPlayer> {
	private SPlayer localPlayer;
	
	public SPlayerFactory(){
		super("Player factory", (byte)5);
	}
	public SPlayer getLocalPlayer() {
		return localPlayer;
	}
	public void setLocalPlayer(SPlayer localPlayer) {
		this.localPlayer = localPlayer;
	}
}
