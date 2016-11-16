package GameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Specifications {
	public static final int FPS_M = 60;
	public static final int DataLength = 32;
	public static final int WindowWidth = 1280;
	public static final int WindowHeight = 768;
	
	public static final List<String> EntityCommands = Arrays.asList("CLIIN","ENTUP","ENTDE");
	public static final List<String> ObjectCommands = Arrays.asList("OBJCR","OBJUP","OBJDE");
	public static final List<String> ServerCommands = Arrays.asList("CNNCL","CNNAP","CNNNA",
																	"DSCCL","PNGRQ","PNGAN");
	public static final List<String> PingCommands = Arrays.asList("PNGRQ","PNGAN");
	public static final List<String> ComCommands = new ArrayList<>(EntityCommands.size()+
												ObjectCommands.size()+ServerCommands.size());
	
	public static String[] resourcePathStrings;
	
	public static void InitSpecifications(){
		ComCommands.addAll(EntityCommands);
		ComCommands.addAll(ObjectCommands);
		ComCommands.addAll(ServerCommands);
		
		
		//TODO create automatic import
		resourcePathStrings = new String[5];
		resourcePathStrings[0] = "res/entity/spaceshipv1.png";
		resourcePathStrings[1] = "res/entity/prob.png";
		resourcePathStrings[2] = "res/dot.png";
		resourcePathStrings[3] = "res/entity/spaceshipv2.png";
		resourcePathStrings[4] = "res/object/background/bg1.png";
	}
}
