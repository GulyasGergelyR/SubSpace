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
import GameEngine.SPlayer;
import GameEngine.Specifications;
import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SObject.ObjectState;
import GameEngine.EntityEngine.SEntity;
import Main.SMain;
import WebEngine.ComEngine.SNode;

//TODO create SDrawObject and replace texture

public class SRenderer {
	private SGameInstance gameInstance;
	// TODO recalculate Mouse positions based on viewport
	private boolean followLocalPlayer = true;
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
		
		SetTextViewPort();
		DrawText();
	}
	
	private void DrawBackGround(){
		Draw(gameInstance.getBackGround().getDrawables());
	}
	private void DrawObjects(){
		for (SObject object : gameInstance.getObjects()){
			if (object.getObjectState() == ObjectState.Active){
				Draw(object.getDrawables());
			}
		}
	}
	private void DrawEntities(){
		List<SEntity> Entities = gameInstance.getEntities();
		for (SEntity entity : Entities){
			if (entity.getObjectState() == ObjectState.Active){
				Draw(entity.getDrawables());
			}
		}
	}
	
	public void setFollowLocalPlayer(boolean follow){
		this.followLocalPlayer = follow;
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
					y-Specifications.WindowHeight/2, -1, 1);
			glMatrixMode(GL_MODELVIEW);
		}
	}
	
	private void DrawText(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,
				Specifications.WindowWidth,
				Specifications.WindowHeight,
				0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		if (!SMain.IsServer()){
			DrawTextOfPlayer(playerToFollow, 0);
		}else{
			if (playerToFollow == null){
				ListIterator<SPlayer> iter = gameInstance.getPlayers().listIterator();
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
			if (SMain.IsServer()){
				font.drawString(30+i*textSize, Specifications.WindowHeight-1790, "Name: "+player.getName(), Color.yellow); //x, y, string to draw, color
				font.drawString(30+i*textSize, Specifications.WindowHeight-170, "Address: "+player.getClientNode().getAddress().toString(), Color.yellow); //x, y, string to draw, color
				font.drawString(30+i*textSize, Specifications.WindowHeight-150, "Pos: "+entity.getPos().getString(), Color.yellow); //x, y, string to draw, color
			} 
			float delta = gameInstance.getDelta();
			if (delta > 0){
				font.drawString(30+i*textSize, Specifications.WindowHeight-130, "fps: "+(int)(1000/delta), Color.yellow); //x, y, string to draw, color
			}
			font.drawString(30+i*textSize, Specifications.WindowHeight-110, "Life: "+entity.getLife(), Color.yellow); //x, y, string to draw, color
			font.drawString(30+i*textSize, Specifications.WindowHeight-90, "kills: "+player.getKills(), Color.yellow); //x, y, string to draw, color
			font.drawString(30+i*textSize, Specifications.WindowHeight-70, "death: "+player.getDeaths(), Color.yellow); //x, y, string to draw, color
			font.drawString(30+i*textSize, Specifications.WindowHeight-50, "ping: "+player.getClientNode().getPing(), Color.yellow); //x, y, string to draw, color
		}
	}
	
	private void deceideWhichPlayerToFollow(){
		if(!SMain.IsServer()){
			playerToFollow = gameInstance.getLocalPlayer();
		}
		else {
			while(Keyboard.next()){
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
				    if (Keyboard.getEventKeyState()) {
				    	ListIterator<SPlayer> iter = gameInstance.getPlayers().listIterator();
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
					size*Specifications.WindowHeight/2, -1, 1);
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
					y+(Specifications.WindowHeight/2)*size, -1, 1);
			glMatrixMode(GL_MODELVIEW);
		}
	}
	
	private static void Draw(List<SRenderObject> drawables){
		for(SRenderObject draw : drawables){
			Draw(draw);
		}
	}
	
	private static void Draw(SRenderObject SRO)
	{
		
		float x = SRO.v.getX();
		float y = SRO.v.getY();
		float scale = SRO.scale;
		float rotateBy = SRO.rotateBy;
		float transparency = SRO.transparency;
		Texture texture = SRO.texture;

		int alpha = (int)(transparency*255);
		Color c = new Color(255,255,255,alpha);
		c.bind();

		texture.bind(); // or GL11.glBind(texture.getTextureID());
		
		glPushMatrix();
		
		glTranslatef(x,y,0);
		glRotatef(rotateBy,0f,0f,1f);
		glScalef(scale,scale,scale);
		glTranslatef(-x,-y,0);
		
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2f(0,0);
			GL11.glVertex2f(x-texture.getTextureWidth()/2,y-texture.getTextureHeight()/2);
			GL11.glTexCoord2f(0,1);
			GL11.glVertex2f(x+texture.getTextureWidth()/2,y-texture.getTextureHeight()/2);
			GL11.glTexCoord2f(1,1);
			GL11.glVertex2f(x+texture.getTextureWidth()/2,y+texture.getTextureHeight()/2);
			GL11.glTexCoord2f(1,0);
			GL11.glVertex2f(x-texture.getTextureWidth()/2,y+texture.getTextureHeight()/2);
		}
		GL11.glEnd();
		glPopMatrix();
	}
}
