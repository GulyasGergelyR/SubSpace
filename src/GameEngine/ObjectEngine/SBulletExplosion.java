package GameEngine.ObjectEngine;

import GameEngine.GeomEngine.SVector;

public class SBulletExplosion extends SExplosion{
	public SBulletExplosion(SVector pos) {
		super(pos);
		this.maxLife = 8;
		this.size = 0.2f;
		this.audioString = "res/audio/single_laser_shot.wav";
	}

	@Override
	protected void playSoundEffect(float speed, float volume) {
		super.playSoundEffect(0.3f, volume);
	}
	
}
