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

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import GameEngine.SGameInstance;
import GameEngine.SPlayer;
import GameEngine.Specifications;
import GameEngine.BaseEngine.SObject;
import GameEngine.BaseEngine.SObject.ObjectState;
import GameEngine.EntityEngine.SEntity;
import Main.SMain;

//TODO create SDrawObject and replace texture

public class SRenderer {
	private SGameInstance gameInstance;
	// TODO recalculate Mouse positions based on viewport
	private boolean followLocalPlayer = true;
	
	public SRenderer(SGameInstance GameInstance){
		this.gameInstance = GameInstance;
	}
	
	public void DrawGame(){
		if (!SMain.IsServer()){
			if(followLocalPlayer) FollowLocalPlayer();
		}
		DrawBackGround();
		DrawObjects();
		DrawEntities();
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
	
	private void FollowLocalPlayer(){
		SPlayer localPlayer = gameInstance.getLocalPlayer();
		SEntity entity = localPlayer.getEntity();
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
					y-Specifications.WindowHeight/2,
					y+Specifications.WindowHeight/2, -1, 1);
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
