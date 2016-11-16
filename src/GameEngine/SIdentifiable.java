package GameEngine;

public class SIdentifiable {
	protected SId Id;
	
	public SIdentifiable(){
		Id = SId.getNewId(this);
	}
	public SIdentifiable(SIdentifiable i){
		Id = i.Id;
	}
	
	public SId getId() {
		return this.Id;
	}
	public void setId(SId id) {
		this.Id.set(id);
	}
	public boolean equals(SIdentifiable identifiable){
		return this.Id.equals(identifiable.Id);
	}
}
