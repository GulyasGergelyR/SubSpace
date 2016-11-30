package GameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Specifications {
	public static final int FPS_M = 60;
	public static final int DataLength = 39;
	public static int WindowWidth = 1280;
	public static int WindowHeight = 768;
	
	public static final List<String> EntityCommands = Arrays.asList("CLIIN","ENTUP","ENTDE");
	public static final List<String> ObjectCommands = Arrays.asList("OBJCR","OBJUP","OBJDE");
	public static final List<String> ServerCommands = Arrays.asList("CNNCL","CNNAP","CNNNA",
																	"DSCCL","PNGRQ","PNGAN");
	public static final List<String> PingCommands = Arrays.asList("PNGRQ","PNGAN");
	public static final List<String> ComCommands = new ArrayList<>(EntityCommands.size()+
												ObjectCommands.size()+ServerCommands.size());
	
	//public static String[] resourcePathStrings;
	public static List<String> resourcePathStrings = new ArrayList<String>();
	public static List<String> audioPathStrings = new ArrayList<String>();
	
	public static void InitSpecifications(){
		ComCommands.addAll(EntityCommands);
		ComCommands.addAll(ObjectCommands);
		ComCommands.addAll(ServerCommands);
		
		//TODO create automatic import
		resourcePathStrings.add("res/entity/spaceshipv1.png");
		resourcePathStrings.add("res/entity/spaceshipv2.png");
		resourcePathStrings.add("res/entity/spaceshipv3.png");
		resourcePathStrings.add("res/dot.png");
		resourcePathStrings.add("res/object/background/bg1.png");
		resourcePathStrings.add("res/object/bullet/bullet.png");
		resourcePathStrings.add("res/object/bullet/yellowbullet.png");
		resourcePathStrings.add("res/object/bullet/bluebullet.png");
		resourcePathStrings.add("res/object/explosion/explosion.png");
		resourcePathStrings.add("res/object/explosion/explosionv2.png");
		resourcePathStrings.add("res/object/powerup/powerupring.png");
		resourcePathStrings.add("res/object/powerup/powerupheal.png");
		resourcePathStrings.add("res/object/wormhole/wormhole.png");
		
		audioPathStrings.add("res/audio/ambient.wav");
		audioPathStrings.add("res/audio/single_laser_shot.wav");
		audioPathStrings.add("res/audio/small_blast.wav");
	}
}
