package GameEngine;

import GameEngine.EntityEngine.SEntity;
import WebEngine.ComEngine.SNode;

public class SId {
	static int currentMaxDefaultObjectId = 1;
	static int currentMaxEntityId = 2;
	static int currentMaxNodeId = 2;
	public static SId getNewId(Object o){
		if (o instanceof SEntity || o instanceof SNode){
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
		if (this.Id == 0 || id.Id == 0){
			System.out.println("Checking not initialized id");
			return false;
		}
		else return (this.Id == id.Id);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Integer.toString(Id);
	}
	
	
}
