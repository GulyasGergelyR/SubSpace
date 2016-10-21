package RenderingEngine;

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
import GameEngine.EntityEngine.SEntity;

public class SRenderer {
	SGameInstance GameInstance;
	public SRenderer(SGameInstance GameInstance){
		this.GameInstance = GameInstance;
	}
	
	public void DrawObjects(){
		DrawEntities();
	}
	
	private void DrawEntities(){
		List<SEntity> Entities = GameInstance.getEntities();
		for (SEntity entity : Entities){
			Draw(entity.Draw());
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
