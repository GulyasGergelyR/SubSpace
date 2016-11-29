package GameEngine.ObjectEngine;

import GameEngine.SPlayer;
import GameEngine.SResLoader;
import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SObject.ObjectState;
import GameEngine.GeomEngine.SVector;
import Main.SMain;

public class SExplosion extends SObject{
	
	protected int currentLife = 0;
	protected int maxLife = 6;
	protected float growing = 0.05f;
	
	protected boolean firstTime = true;
	
	public SExplosion(SVector pos){
		super();
		this.pos = new SVector(pos);
		this.getBody().setTexture("res/object/explosion/explosion.png");
		this.getBody().setScale(0.0f);
	}

	@Override
	public void update() {
		if (firstTime && !SMain.IsServer()){
			SPlayer localPlayer = SMain.getGameInstance().getLocalPlayer();
			if (localPlayer.getEntity().getObjectState().equals(ObjectState.Active))
			{
				SVector playerPos = localPlayer.getEntity().getPos();
				float dist = playerPos.d(pos);
				float maxDistance = 2000;
				if (dist < maxDistance){
					float maxVolume = 0.5f;
					float volume = (maxDistance-dist)*maxVolume/maxDistance;
					SResLoader.getAudio("res/audio/small_blast.wav").playAsSoundEffect(1.0f, volume, false);
				}
			}
		}
		
		this.getBody().setScale(this.getBody().getScale()+growing);
		currentLife++;
		if(currentLife>=maxLife){
			this.setObjectState(ObjectState.WaitingDelete);
		}
	}
}
