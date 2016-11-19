package GameEngine;

import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public abstract class SResLoader {

	static String[] location = new String[100];
	static Texture[] textures = new Texture[100];

	static int N = 0;

	public static void addSpriteArray(String[] s) {
		N = s.length;
		for (int i = 0; i <s.length; i++) {
			if (s[i].length()>0) addSprite(s[i]);
		}
	}

	public static void addSprite(String s) {
		location[N] = s;
		textures[N] = initResources(location[N]);
		N++;
	}

	public static void print() {
		for (int i = 0; i < N; i++) {
			System.out.println(location[i]);
		}
	}

	public static Texture getTexture(String s) {
		int i = 0;
		while (i < N) {
			if (location[i] == s)
				return (textures[i]);
			i++;
		}
		System.out.println("No PNG found with name: "+s);
		return getTexture("res/dot.png");
	}

	public static Texture initResources(String s) {
		Texture texture;
		try {
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(s));
			return texture;
		} catch (IOException ex) {
			ex.printStackTrace();
			Display.destroy();
			System.exit(0);
		}
		return null;
	}
}

