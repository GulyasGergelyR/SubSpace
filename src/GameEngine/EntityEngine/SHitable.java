package GameEngine.EntityEngine;

import GameEngine.BaseEngine.SUpdatable;

public interface SHitable {
	public boolean gotHit(float damage, SUpdatable source);
}
