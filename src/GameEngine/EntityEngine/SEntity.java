package GameEngine.EntityEngine;

import java.util.List;

import GameEngine.SResLoader;
import GameEngine.GeomEngine.SVector;
import RenderingEngine.SRenderObject;

public class SEntity extends GameEngine.BaseEngine.SMobile{
	public SEntity(){
		super();
		this.pos = new SVector(250.0f,250.0f);
		this.texture = "res/entity/spaceshipv1.png";
		this.scale = 0.05f;
		this.setController(new SHumanControl(this));
	}

	@Override
	public List<SRenderObject> Draw() {
		// TODO Auto-generated method stub
		List<SRenderObject> list = super.Draw();
		list.add(new SRenderObject("res/dot.png", pos, 0.0f, 0.5f, 1.0f));
		return list;
	}
	
}
