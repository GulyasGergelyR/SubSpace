package GameEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class Specifications {
	public static final int FPS_M = 60;
	public static final int DataLength = 41;
	public static int WindowWidth = 1280;
	public static int WindowHeight = 768;
	
	//public static String[] resourcePathStrings;
	public static List<String> resourcePathStrings = new ArrayList<String>();
	public static List<String> audioPathStrings = new ArrayList<String>();
	
	private static void parseDir(String path){
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  if (listOfFiles[i].getName().contains(".wav")){
		    		  audioPathStrings.add(listOfFiles[i].getPath());
		    	  } else if (listOfFiles[i].getName().contains(".ini")){
		    		  continue;
		    	  } else {
		    		  resourcePathStrings.add(listOfFiles[i].getPath());
		    	  }
		      } else if (listOfFiles[i].isDirectory()) {
		    	  parseDir(listOfFiles[i].getPath());
		      }
		    }
	}
	
	public static void InitSpecifications(){
		parseDir("res");
	}
}
