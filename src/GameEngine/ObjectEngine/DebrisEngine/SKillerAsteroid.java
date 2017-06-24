package GameEngine.ObjectEngine.DebrisEngine;

import org.newdawn.slick.Color;

import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;

public class SKillerAsteroid extends SAsteroid {
	SEntity targetEntity;
	
	public SKillerAsteroid(SVector pos, SVector moveDir, float scale, SEntity targetEntity){
		super(pos, moveDir, scale);
		getBody().setColor(new Color(1.0f, 0.0f, 0.0f));
		this.targetEntity = targetEntity;
	}
}
