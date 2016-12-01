package RenderingEngine;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import GameEngine.SPlayer.PlayerState;
import GameEngine.SPlayer;
import GameEngine.Specifications;
import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SObject.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUpHeal;
import Main.SMain;

public class SHud {
	public static List<SRenderObject> DrawHud(){
		List<SRenderObject> drawables = new ArrayList<SRenderObject>(10);
		int miniMapSize = 250;
		
		drawables.add(new SRenderObject("res/hud/MiniMap.png",new SVector(miniMapSize/2.0f, Specifications.WindowHeight-miniMapSize/2.0f), 0.0f, ((float)miniMapSize)/512.0f, 0.7f, new Color(255,255,255,0)));
		for(SEntity entity : SMain.getGameInstance().getEntities()){
			if (entity.getObjectState().equals(ObjectState.Active)){
				float x = (entity.getPos().getX()+5*1024)/5/2048 * miniMapSize;
				float y = (entity.getPos().getY()+5*1024)/5/2048 * miniMapSize;
				if (x<0 || x > miniMapSize || y < 0 || y> miniMapSize)
					continue;
				if (entity.getPlayer().getPlayerState().equals(PlayerState.local)){
					drawables.add(new SRenderObject("res/hud/MiniMapLocalPlayer.png",new SVector(x, Specifications.WindowHeight-y), -entity.getLookDir().getAngle(), 0.5f, 0.7f, new Color(255,255,255,0)));
					SVector leftBottom = new SVector((entity.getMaxLife()-entity.getLife())/entity.getMaxLife()*0.5f,0.0f);
					SVector rightUpper = new SVector(0.5f+(entity.getMaxLife()-entity.getLife())/entity.getMaxLife()*0.5f,1.0f);
					int hudHealthBarSize = 128;
					drawables.add(new SRenderObject("res/hud/HudHealthBar.png", new SVector(hudHealthBarSize/2.0f, Specifications.WindowHeight-miniMapSize-32.0f/2.0f*((float)hudHealthBarSize)/128.0f), -90.0f,1.0f,0.7f, new Color(255,255,255,0), leftBottom, rightUpper));
					drawables.add(new SRenderObject("res/hud/HudHealthBarGrid.png",new SVector(hudHealthBarSize/2.0f, Specifications.WindowHeight-miniMapSize-32.0f/2.0f*((float)hudHealthBarSize)/128.0f),
							-90.0f, ((float)hudHealthBarSize)/128.0f, 0.7f, new Color(255,255,255,0)));					
				}
				else 
					drawables.add(new SRenderObject("res/hud/MiniMapLanPlayer.png",new SVector(x, Specifications.WindowHeight-y), -entity.getLookDir().getAngle(), 0.5f, 0.7f, new Color(255,255,255,0)));
			}
		}
		for(SObject object : SMain.getGameInstance().getObjects()){
			if (object instanceof SPowerUpHeal){
				if (object.getObjectState().equals(ObjectState.Active)){
					float x = (object.getPos().getX()+5*1024)/5/2048 * miniMapSize;
					float y = (object.getPos().getY()+5*1024)/5/2048 * miniMapSize;
					drawables.add(new SRenderObject("res/hud/MiniMapPowerUpHeal.png",new SVector(x, Specifications.WindowHeight-y), 0.0f, 0.5f, 0.7f, new Color(255,255,255,0)));
				}
			}
		}
		
		return drawables;
	}
}
