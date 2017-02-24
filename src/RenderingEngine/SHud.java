package RenderingEngine;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;

import GameEngine.SPlayer.PlayerType;
import GameEngine.Specifications;
import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SUpdatable;
import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFH;
import GameEngine.ObjectEngine.DebrisEngine.SAsteroid;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUpFactory;
import GameEngine.ObjectEngine.PowerUpEngine.SPowerUpHeal;
import Main.SMain;

public class SHud {
	public static List<SRenderObject> DrawHud(){
		List<SRenderObject> drawables = new ArrayList<SRenderObject>(10);
		int miniMapSize = 250;
		
		drawables.add(new SRenderObject("res/hud/MiniMap.png",new SVector(miniMapSize/2.0f, Specifications.WindowHeight-miniMapSize/2.0f), 0.0f, ((float)miniMapSize)/512.0f, 0.7f, new Color(255,255,255,0), 8f));
		for(SEntity entity : SMain.getGameInstance().getEntities()){
			if (entity.getObjectState().equals(ObjectState.Active)){
				// Life Bar
				SVector leftBottomLife = new SVector((entity.getMaxLife()-entity.getLife())/entity.getMaxLife()*0.5f,0.0f);
				SVector rightUpperLife = new SVector(0.5f+(entity.getMaxLife()-entity.getLife())/entity.getMaxLife()*0.5f,1.0f);
				int hudHealthBarSize = 128;
				drawables.add(new SRenderObject("res/hud/HudHealthBar.png", new SVector(hudHealthBarSize/2.0f, Specifications.WindowHeight-miniMapSize-32.0f/2.0f*((float)hudHealthBarSize)/128.0f), -90.0f,1.0f,0.7f, new Color(255,255,255,0), leftBottomLife, rightUpperLife, 8f));
				drawables.add(new SRenderObject("res/hud/HudHealthBarGrid.png",new SVector(hudHealthBarSize/2.0f, Specifications.WindowHeight-miniMapSize-32.0f/2.0f*((float)hudHealthBarSize)/128.0f),
						-90.0f, ((float)hudHealthBarSize)/128.0f, 0.7f, new Color(255,255,255,0), 8.1f));
				// Shield Bar
				SVector leftBottomShield = new SVector((entity.getMaxShield()-entity.getShield())/entity.getMaxShield()*0.5f,0.0f);
				SVector rightUpperShield = new SVector(0.5f+(entity.getMaxShield()-entity.getShield())/entity.getMaxShield()*0.5f,1.0f);
				int hudShieldBarSize = 128;
				drawables.add(new SRenderObject("res/hud/HudShieldBar.png", new SVector(hudShieldBarSize/2.0f, Specifications.WindowHeight-miniMapSize-100.0f/2.0f*((float)hudShieldBarSize)/128.0f), -90.0f,1.0f,0.7f, new Color(255,255,255,0), leftBottomShield, rightUpperShield, 8f));
				drawables.add(new SRenderObject("res/hud/HudHealthBarGrid.png",new SVector(hudShieldBarSize/2.0f, Specifications.WindowHeight-miniMapSize-100.0f/2.0f*((float)hudShieldBarSize)/128.0f),
						-90.0f, ((float)hudHealthBarSize)/128.0f, 0.7f, new Color(255,255,255,0), 8.1f));
				
				float x = (entity.getPos().getX()+5*1024)/5/2048 * miniMapSize;
				float y = (entity.getPos().getY()+5*1024)/5/2048 * miniMapSize;
				if (x<0 || x > miniMapSize || y < 0 || y> miniMapSize)
					continue;
				if (entity.getPlayer().getPlayerType().equals(PlayerType.local)){
					drawables.add(new SRenderObject("res/hud/MiniMapLocalPlayer.png",new SVector(x, Specifications.WindowHeight-y), -entity.getLookDir().getAngle(), 0.5f, 1.0f, new Color(255,255,255,0), 8.2f));				
				}
				else 
					drawables.add(new SRenderObject("res/hud/MiniMapLanPlayer.png",new SVector(x, Specifications.WindowHeight-y), -entity.getLookDir().getAngle(), 0.5f, 1f, new Color(255,255,255,0), 8.2f));
			}
		}
		for(SObject object : SPowerUpFactory.getObjects()){
			String res = "";
			if (object instanceof SPowerUpHeal){
				res =  "res/hud/MiniMapPowerUpHeal.png";
			} else {
				res =  "res/hud/MiniMapPowerUpBox.png";
			}
			if (object.getObjectState().equals(ObjectState.Active)){
				float x = (object.getPos().getX()+5*1024)/5/2048 * miniMapSize;
				float y = (object.getPos().getY()+5*1024)/5/2048 * miniMapSize;
				drawables.add(new SRenderObject(res,new SVector(x, Specifications.WindowHeight-y), 0.0f, 0.5f, 1.0f, new Color(255,255,255,0), 8.1f));
			}
		}
		
		for(SUpdatable object : SFH.Debris.getObjects()){
			if (object instanceof SAsteroid){
				object = (SAsteroid) object;
				if (object.getObjectState().equals(ObjectState.Active)){
					float x = (object.getPos().getX()+5*1024)/5/2048 * miniMapSize;
					float y = (object.getPos().getY()+5*1024)/5/2048 * miniMapSize;
					drawables.add(new SRenderObject("res/hud/MiniMapAsteroid.png",new SVector(x, Specifications.WindowHeight-y), object.getLookDir().getAngle(), 0.2f * object.getBody().getCurrentDrawScale(), 0.2f, new Color(255,255,255,0), 8.05f));
				}
			}
		}
		
		return drawables;
	}
}
