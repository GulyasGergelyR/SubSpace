package GameEngine.EntityEngine;

import GameEngine.ObjectEngine.SFactory;

public class SEntityFactory extends SFactory<SEntity> {
	public SEntityFactory(){
		super("Entity factory", (byte)10);
	}
}
