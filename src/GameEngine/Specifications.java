package GameEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Specifications {
	public static final int FPS_M = 60;
	public static final int DataLength = 64;
	public static final List<String> EntityCommands = Arrays.asList("ENTIN","ENTUP","ENTDE");
	public static final List<String> ObjectCommands = Arrays.asList("OBJUP","OBJDE");
	public static final List<String> ServerCommands = Arrays.asList("CNNCL","DSCCL","PNGRQ","PNGAN");
	public static final List<String> ComCommands = new ArrayList<>(EntityCommands.size()+
												ObjectCommands.size()+ServerCommands.size());
	
	public static void InitSpecifications(){
		ComCommands.addAll(EntityCommands);
		ComCommands.addAll(ObjectCommands);
		ComCommands.addAll(ServerCommands);
	}
}
