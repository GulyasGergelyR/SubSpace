package GameEngine;

import GameEngine.EntityEngine.SEntity;
import WebEngine.ComEngine.SNode;

public class SId {
	static int currentMaxDefaultObjectId = 1;
	static int currentMaxEntityId = 1;
	static int currentMaxNodeId = 1;
	public static SId getNewId(Object o){
		if (o instanceof SEntity){
			return new SId(currentMaxEntityId++);
		}
		else if (o instanceof SNode){
			return new SId(currentMaxNodeId++);
		}
		else{
			return new SId(currentMaxDefaultObjectId++);
		}
	}
	private int Id;
	public SId(int id){
		this.Id = id;
	}
	public int get() {
		return Id;
	}
	public void set(int id) {
		Id = id;
	}
	public void set(SId id) {
		Id = id.Id;
	}
	public boolean equals(SId id){
		return (this.Id == id.Id);
	}
	
}
