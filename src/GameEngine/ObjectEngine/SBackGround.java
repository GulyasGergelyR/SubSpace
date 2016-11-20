package GameEngine.ObjectEngine;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.Size2DSyntax;

import GameEngine.BaseEngine.SObject;
import GameEngine.GeomEngine.SVector;
import RenderingEngine.SRenderObject;

public class SBackGround extends SObject{
	
	public SBackGround(){
		super();
		this.getBody().setTexture("res/object/background/bg1.png");
		this.getBody().setScale(1.0f); //1024
	}

	@Override
	public List<SRenderObject> getDrawables() {
		List<SRenderObject> list = new ArrayList<SRenderObject>();
		int size = 5;
		float picSize = 2048 * getBody().getScale();
		for (int i=0;i<size; i++)
			for (int j=0;j<size; j++){
				list.add(new SRenderObject(body.getTexture(), new SVector((i-(size/2))*picSize,(j-(size/2))*picSize), lookDir.getAngle(), body.getScale(), 1.0f));
			}
		return list;
	}
	
	
}
