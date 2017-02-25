package GameEngine;

public class SIdentifiable {
	protected SId Id;
	
	public SIdentifiable(){
		Id = SId.getNewId(this);
	}
	public SIdentifiable(int i){
		Id = new SId(i);
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
	public boolean equals(SId id){
		return this.Id.equals(id);
	}
	public boolean equals(int id){
		return (this.Id.equals(id));
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SIdentifiable){
			return equals((SIdentifiable)obj);
		}
		else if (obj instanceof SId){
			return equals((SId)obj);
		}
		else if (obj instanceof Integer){
			return equals(((Integer)obj).intValue());
		}
		return false;
	}
	public void inheritIdFrom(SIdentifiable identifiable){
		this.Id = identifiable.Id;
	}
	
}
