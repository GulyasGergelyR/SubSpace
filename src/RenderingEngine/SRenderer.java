package RenderingEngine;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.awt.Font;
import java.util.List;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import GameEngine.SGameInstance;
import GameEngine.Specifications;
import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SUpdatable.ObjectState;
import GameEngine.EntityEngine.SEntity;
import GameEngine.ObjectEngine.SFH;
import GameEngine.PlayerEngine.SPlayer;
import Main.SMain;

//TODO create SDrawObject and replace texture

public class SRenderer {
	private SGameInstance gameInstance;
	// TODO recalculate Mouse positions based on viewport
	private SPlayer playerToFollow;
	TrueTypeFont font;
	
	public SRenderer(SGameInstance GameInstance){
		this.gameInstance = GameInstance;
		Font awtFont = new Font("Times New Roman", Font.BOLD, 15); //name, style (PLAIN, BOLD, or ITALIC), size
		font = new TrueTypeFont(awtFont, false); //base Font, anti-aliasing true/false
	} 
	
	public void DrawGame(){
		deceideWhichPlayerToFollow();
		FollowPlayer();
		DrawBackGround();
		DrawObjects();
		DrawEntities();
		DrawExplosions();
		
		SetTextViewPort();
		DrawText();
		
		DrawHud();
	}
	
	private void DrawBackGround(){
		Draw(gameInstance.getBackGround().getDrawables());
	}
	private void DrawObjects(){
		for (SObject object : SFH.Bullets.getObjects()){
			if (object.getObjectState() == ObjectState.Active){
				Draw(object.getDrawables());
			}
		}
		for (SObject object : SFH.PowerUps.getObjects()){
			if (object.getObjectState() == ObjectState.Active){
				Draw(object.getDrawables());
			}
		}
		for (SObject object : SFH.Debris.getObjects()){
			if (object.getObjectState() == ObjectState.Active){
				Draw(object.getDrawables());
			}
		}
	}
	private void DrawEntities(){
		List<SEntity> Entities = SFH.Entities.getObjects();
		for (SEntity entity : Entities){
			if (entity.getObjectState() == ObjectState.Active){
				Draw(entity.getDrawables());
			}
		}
	}
	private void DrawExplosions(){
		for (SObject object : gameInstance.getAnimationObjects()){
			if (object.getObjectState() == ObjectState.Active){
				Draw(object.getDrawables());
			}
		}
	}
	
	private void SetTextViewPort(){
		if (playerToFollow == null){
			return;
		}
		SEntity entity = playerToFollow.getEntity();
		if (entity == null)
			return;
		if(entity.getObjectState().equals(ObjectState.Active) 
				|| entity.getObjectState().equals(ObjectState.Ghost)){
			float x = entity.getPos().getX();
			float y = entity.getPos().getY();
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(x-Specifications.WindowWidth/2,
					x+Specifications.WindowWidth/2,
					y+Specifications.WindowHeight/2,
					y-Specifications.WindowHeight/2, -10, 10);
			glMatrixMode(GL_MODELVIEW);
		}
	}
	
	private void DrawText(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,
				Specifications.WindowWidth,
				Specifications.WindowHeight,
				0, -10, 10);
		glMatrixMode(GL_MODELVIEW);
		
		if (!SMain.IsServer()){
			DrawTextOfPlayer(playerToFollow, 0);
		}else{
			if (playerToFollow == null){
				ListIterator<SPlayer> iter = SFH.Players.getObjects().listIterator();
				int i=0;
				while(iter.hasNext()){
					SPlayer player = iter.next();
					DrawTextOfPlayer(player, i);
					i++;
				}
			}else{
				DrawTextOfPlayer(playerToFollow, 0);
			}
		}
		// Draw name above player
		for (SEntity entity : SFH.Entities.getObjects()){
			if (entity.isActive()){
				float x;
				float y;
				if (playerToFollow == null){
					x = Specifications.WindowWidth / 2f + 0.1f*(entity.getPos().getX());
					y = Specifications.WindowHeight / 2f - 0.1f*(entity.getPos().getY());
					x -= font.getWidth(entity.getPlayer().getName())/2f;
					y -= 5;
				} else {
					x = Specifications.WindowWidth / 2f + 0.5f*(entity.getPos().getX() - playerToFollow.getEntity().getPos().getX());
					y = Specifications.WindowHeight / 2f - 0.5f*(entity.getPos().getY() - playerToFollow.getEntity().getPos().getY());
					x -= font.getWidth(entity.getPlayer().getName())/2f;
					y -= 52;
				}
				font.drawString(x, y, entity.getPlayer().getName(), Color.green); //x, y, string to draw, color
			}
		}
	}
	
	private void DrawTextOfPlayer(SPlayer player, int i){
		if (player == null)
			return;
		SEntity entity = player.getEntity();
		if (entity == null)
			return;
		int textSize = 200;
		if(entity.getObjectState().equals(ObjectState.Active) 
				|| entity.getObjectState().equals(ObjectState.Ghost)){
			float textPos = Specifications.WindowHeight - 500;
			if (SMain.IsServer()){
				font.drawString(30+i*textSize, textPos, "Name: "+player.getName(), Color.yellow); //x, y, string to draw, color
				font.drawString(30+i*textSize, textPos+20, "Address: "+player.getClientNode().getAddress().toString(), Color.yellow); //x, y, string to draw, color
				font.drawString(30+i*textSize, textPos+40, "Pos: "+entity.getPos().getString(), Color.yellow); //x, y, string to draw, color
			}
			float delta = gameInstance.getDelta();
			if (delta > 0){
				font.drawString(30+i*textSize, textPos+60, "fps: "+(int)(1000/delta), Color.yellow); //x, y, string to draw, color
			}
			font.drawString(30+i*textSize, textPos+80, "Life: "+entity.getLife(), Color.yellow); //x, y, string to draw, color
			font.drawString(30+i*textSize, textPos+100, "Kills: "+player.getKills(), Color.yellow); //x, y, string to draw, color
			font.drawString(30+i*textSize, textPos+120, "Death: "+player.getDeaths(), Color.yellow); //x, y, string to draw, color
			font.drawString(30+i*textSize, textPos+140, "Ping: "+player.getClientNode().getPing(), Color.yellow); //x, y, string to draw, color
		}
	}
	
	//Only serverside
	private void deceideWhichPlayerToFollow(){
		if(!SMain.IsServer()){
			playerToFollow = SFH.Players.getLocalPlayer();
		}
		else {
			while(Keyboard.next()){
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
				    if (Keyboard.getEventKeyState()) {
				    	ListIterator<SPlayer> iter = SFH.Players.getObjects().listIterator();
						while(iter.hasNext()){
							SPlayer player = iter.next();
							if(playerToFollow == null){
								playerToFollow = player;
								break;
							}
						    if(player.equals(playerToFollow)){
						        if(iter.hasNext()){
						        	playerToFollow = iter.next();
						        }
						        else{
						        	playerToFollow = null;
						        }
						    }
						}
					break;
				    }
				}

			}
		}
	}
	
	private void FollowPlayer(){
		if (playerToFollow == null){
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			int size = 10;
			glOrtho(-size*Specifications.WindowWidth/2,
					size*Specifications.WindowWidth/2,
					-size*Specifications.WindowHeight/2,
					size*Specifications.WindowHeight/2, -10, 10);
			glMatrixMode(GL_MODELVIEW);
			return;
		}
		SEntity entity = playerToFollow.getEntity();
		if (entity == null)
			return;
		if(entity.getObjectState().equals(ObjectState.Active) 
				|| entity.getObjectState().equals(ObjectState.Ghost)){
			float x = entity.getPos().getX();
			float y = entity.getPos().getY();
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			int size = 2;
			glOrtho(x-(Specifications.WindowWidth/2)*size,
					x+(Specifications.WindowWidth/2)*size,
					y-(Specifications.WindowHeight/2)*size,
					y+(Specifications.WindowHeight/2)*size, -10, 10);
			glMatrixMode(GL_MODELVIEW);
		}
	}
	
	private static void Draw(List<SRenderObject> drawables){
		for(SRenderObject draw : drawables){
			Draw(draw);
		}
	}
	
	private static void DrawHud(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,
				Specifications.WindowWidth,
				Specifications.WindowHeight,
				0, -10, 10);
		glMatrixMode(GL_MODELVIEW);
		
		for(SRenderObject draw : SHud.DrawHud()){
			Draw(draw);
		}
	}
	
	private static void Draw(SRenderObject SRO)
	{
		float x = SRO.v.getX();
		float y = SRO.v.getY();
		float scale = SRO.scale;
		float rotateBy = SRO.rotateBy;
		//float transparency = SRO.transparency;
		Texture texture = SRO.texture;
		/*
		int alpha = (int)(transparency*255);
		Color c = new Color(255,255,255,alpha);
		c.bind();
		 */
		SRO.color.bind();
		
		texture.bind(); // or GL11.glBind(texture.getTextureID());
		
		glPushMatrix();
		
		glTranslatef(x,y,0);
		glRotatef(rotateBy,0f,0f,1f);
		glScalef(scale,scale,scale);
		glTranslatef(-x,-y,0);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(SRO.leftBottom.getX(),SRO.leftBottom.getY()); //0,0
			GL11.glVertex3f(x-texture.getTextureWidth()/2,y-texture.getTextureHeight()/2, SRO.get_Z());
			GL11.glTexCoord2f(SRO.leftBottom.getX(),SRO.rightUpper.getY()); //0,1
			GL11.glVertex3f(x+texture.getTextureWidth()/2,y-texture.getTextureHeight()/2, SRO.get_Z());
			GL11.glTexCoord2f(SRO.rightUpper.getX(),SRO.rightUpper.getY()); //1,1
			GL11.glVertex3f(x+texture.getTextureWidth()/2,y+texture.getTextureHeight()/2, SRO.get_Z());
			GL11.glTexCoord2f(SRO.rightUpper.getX(),SRO.leftBottom.getY()); //1,0
			GL11.glVertex3f(x-texture.getTextureWidth()/2,y+texture.getTextureHeight()/2, SRO.get_Z());
		}
		GL11.glEnd();
		glPopMatrix();
	}
}
