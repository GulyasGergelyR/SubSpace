package GameEngine;

import java.util.ArrayList;
import java.util.List;

import GameEngine.EntityEngine.SEntity;
import GameEngine.SyncEngine.SFPS;

public class SGameInstance {
	private List<SEntity> Entities = new ArrayList<SEntity>();
	private SFPS FPS;
	
	
	public SGameInstance(){
		FPS = new SFPS();
	}
	
	public List<SEntity> getEntities(){
		return Entities;
	}
	
	public int getDelta(){
		return FPS.getDelta();
	}
}
