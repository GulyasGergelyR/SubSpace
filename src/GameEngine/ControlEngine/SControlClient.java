package GameEngine.ControlEngine;


public class SControlClient<T> extends SControl<T>{
	public SControlClient(T mobile){
		super(mobile);
	}

	@Override
	public void ThinkAndAct() {
		Think();
	}
}
