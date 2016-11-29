package GameEngine;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public abstract class SResLoader {
	
	static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	static HashMap<String, Audio> audios = new HashMap<String, Audio>();

	public static void addSpriteArray(List<String> resources) {
		for(String s:resources)
			 addSprite(s);
	}
	
	public static void addAudioArray(List<String> resources) {
		for(String s:resources)
			 addAudio(s);
	}

	public static void addSprite(String s) {
		textures.put(s,initTexture(s));
	}
	
	public static void addAudio(String s) {
		audios.put(s,initAudio(s));
	}

	public static Texture getTexture(String s) {
		if (textures.containsKey(s)){
			return textures.get(s);
		}
		System.out.println("No PNG found with name: "+s);
		return getTexture("res/dot.png");
	}
	
	public static Audio getAudio(String s) {
		if (audios.containsKey(s)){
			return audios.get(s);
		}
		System.out.println("No Audio found with name: "+s);
		return getAudio("res/audio/single_laser_shot.wav");
	}

	public static Texture initTexture(String s) {
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
	public static Audio initAudio(String s) {
		Audio audio;
		try {
			audio = AudioLoader.getAudio("WAV",
					ResourceLoader.getResourceAsStream(s));
			return audio;
		} catch (IOException ex) {
			ex.printStackTrace();
			Display.destroy();
			System.exit(0);
		}
		return null;
	}
}

