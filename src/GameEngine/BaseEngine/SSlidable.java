package GameEngine.BaseEngine;

import GameEngine.GeomEngine.SVector;

public class SSlidable extends SMobile {
	protected SVector AimDir;
	
	public SSlidable(){
		super();
	}

	public SVector getAimDir() {
		return AimDir;
	}
	public void setAimDir(SVector aimDir) {
		AimDir = aimDir;
	}
	
}
