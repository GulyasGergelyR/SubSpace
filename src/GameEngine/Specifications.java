package GameEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Specifications {
	public static final int FPS_M = 60;
	public static final int DataLength = 41;
	
	//Player
	public static String name = "Player";
	
	//Connection
	public static String serverIP = "127.0.0.1";
	
	//Graphics
	public static boolean biggestAvailable = true;
	public static boolean fullScreen = true;
	public static int WindowWidth = 1024;
	public static int WindowHeight = 768;
	
	//Debris
	public static int maxNumberOfAsteroids = 120;
	public static float chanceForAsteroid = 0.8f;
	public static float asteroidMaxSpeed = 130;
	
	//Power ups
	public static boolean allowPowerUps = true;
	public static int maxNumberOfPowerUps = 15;
	public static int maxNumberOfPowerUpBull = 3;
	public static float chanceForBull = 0.9994f;
	public static int powerupBullDuration = 1000;
	public static int maxNumberOfPowerUpForceBoost = 3;
	public static float chanceForForceBoost = 0.9994f;
	public static int powerupForceBoostDuration = 1000;
	public static int maxNumberOfPowerUpBurst = 3;
	public static float chanceForBurst = 0.9994f;
	public static int powerupBurstDuration = 1000;
	public static int maxNumberOfPowerUpHeal = 2;
	public static float chanceForHeal = 0.995f;
	public static int powerupHealDuration = 1500;
	public static int powerupHealAmount = 100;
	
	//Effects
	public static int effectBurstDuration = 550;
	public static int effectBullDuration = 600;
	public static int effectForceBoostDuration = 400;
	
	//Entity
	public static int entityLife = 100;
	public static int entityShield = 40;
	public static float entityMass = 0.5f;
	public static float entityMaxSpeed = 55f;
	public static float entityMaxAccl = 12f;
	
	
	//Bullet
	public static int bulletDamage = 8;
	public static float bulletSpeed = 200;
	
	
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
	
	private static void parseConfig(){
		try {
			FileInputStream fis = new FileInputStream("config.cfg");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			parseConfigParams(br);
		} catch (FileNotFoundException e) {
			System.out.println("Could not find config, creating default");
			createConfig();
		} catch (IOException e) {
			
		}
		createConfig();
	}
	
	public static void saveConfig(){
		createConfig();
	}
	
	private static void createConfig(){
		FileWriter fw;
		try {
			fw = new FileWriter("config.cfg");
			printDefaultConfig(fw);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void print(FileWriter fw, String s){
		try {
			fw.write(s+System.getProperty("line.separator"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void printDefaultConfig(FileWriter fw){
		print(fw, "[[Client]]");
		print(fw, "[Player]");
		print(fw, "name="+name);
		print(fw, "[Connection]");
		print(fw, "serverIP="+serverIP);
		print(fw, "[Graphics]");
		print(fw, "biggestAvailable="+biggestAvailable);
		print(fw, "fullScreen="+fullScreen);
		print(fw, "WindowWidth="+WindowWidth);
		print(fw, "WindowHeight="+WindowHeight);
		
		print(fw, "[[Server]]");
		print(fw, "[Debris]");
		print(fw, "maxNumberOfAsteroids="+maxNumberOfAsteroids);
		print(fw, "[PowerUps]");
		print(fw, "allowPowerUps="+allowPowerUps);
		print(fw, "maxNumberOfPowerUps="+maxNumberOfPowerUps);
		print(fw, "maxNumberOfPowerUpBull="+maxNumberOfPowerUpBull);
		print(fw, "chanceForBull="+chanceForBull);
		print(fw, "powerupBullDuration="+powerupBullDuration);
		print(fw, "maxNumberOfPowerUpForceBoost="+maxNumberOfPowerUpForceBoost);
		print(fw, "chanceForForceBoost="+chanceForForceBoost);
		print(fw, "powerupForceBoostDuration="+powerupForceBoostDuration);
		print(fw, "maxNumberOfPowerUpBurst="+maxNumberOfPowerUpBurst);
		print(fw, "chanceForBurst="+chanceForBurst);
		print(fw, "powerupBurstDuration="+powerupBurstDuration);
		print(fw, "maxNumberOfPowerUpHeal="+maxNumberOfPowerUpHeal);
		print(fw, "chanceForHeal="+chanceForHeal);
		print(fw, "powerupHealDuration="+powerupHealDuration);
		print(fw, "powerupHealAmount="+powerupHealAmount);
		print(fw, "[Effects]");
		print(fw, "effectBullDuration="+effectBullDuration);
		print(fw, "effectBurstDuration="+effectBurstDuration);
		print(fw, "effectForceBoostDuration="+effectForceBoostDuration);
		print(fw, "[Entity]");
		print(fw, "entityLife="+entityLife);
		print(fw, "entityShield="+entityShield);
		print(fw, "[Bullet]");
		print(fw, "bulletDamage="+bulletDamage);
	}
	
	private static void parseConfigParams(BufferedReader br) throws IOException{
		String line = null;
		System.out.println("Reading Config...");
		while((line = br.readLine()) != null){
			name = (new SMatcher<String>()).match(line, "name=(.*)", name);
			
			serverIP = (new SMatcher<String>()).match(line, "serverIP=(.*)", serverIP);
			
			biggestAvailable = (new SMatcher<Boolean>()).match(line, "biggestAvailable=(.*)", biggestAvailable);
			fullScreen = (new SMatcher<Boolean>()).match(line, "fullScreen=(.*)", fullScreen);
			WindowWidth = (new SMatcher<Integer>()).match(line, "WindowWidth=(.*)", WindowWidth);
			WindowHeight = (new SMatcher<Integer>()).match(line, "WindowHeight=(.*)", WindowHeight);
			
			maxNumberOfAsteroids = (new SMatcher<Integer>()).match(line, "maxNumberOfAsteroids=(.*)", maxNumberOfAsteroids);
			
			allowPowerUps = (new SMatcher<Boolean>()).match(line, "allowPowerUps=(.*)", allowPowerUps);
			maxNumberOfPowerUps = (new SMatcher<Integer>()).match(line, "maxNumberOfPowerUps=(.*)", maxNumberOfPowerUps);
			maxNumberOfPowerUpBull = (new SMatcher<Integer>()).match(line, "maxNumberOfPowerUpBull=(.*)", maxNumberOfPowerUpBull);
			chanceForBull = (new SMatcher<Float>()).match(line, "chanceForBull=(.*)", chanceForBull);
			powerupBullDuration = (new SMatcher<Integer>()).match(line, "powerupBullDuration=(.*)", powerupBullDuration);
			maxNumberOfPowerUpForceBoost = (new SMatcher<Integer>()).match(line, "maxNumberOfPowerUpForceBoost=(.*)", maxNumberOfPowerUpForceBoost);
			chanceForForceBoost = (new SMatcher<Float>()).match(line, "chanceForForceBoost=(.*)", chanceForForceBoost);
			powerupForceBoostDuration = (new SMatcher<Integer>()).match(line, "powerupForceBoostDuration=(.*)", powerupForceBoostDuration);
			maxNumberOfPowerUpBurst = (new SMatcher<Integer>()).match(line, "maxNumberOfPowerUpBurst=(.*)", maxNumberOfPowerUpBurst);
			chanceForBurst = (new SMatcher<Float>()).match(line, "chanceForBurst=(.*)", chanceForBurst);
			powerupBurstDuration = (new SMatcher<Integer>()).match(line, "powerupBurstDuration=(.*)", powerupBurstDuration);
			maxNumberOfPowerUpHeal = (new SMatcher<Integer>()).match(line, "maxNumberOfPowerUpHeal=(.*)", maxNumberOfPowerUpHeal);
			chanceForHeal = (new SMatcher<Float>()).match(line, "chanceForHeal=(.*)", chanceForHeal);
			powerupHealDuration = (new SMatcher<Integer>()).match(line, "powerupHealDuration=(.*)", powerupHealDuration);
			powerupHealAmount = (new SMatcher<Integer>()).match(line, "powerupHealAmount=(.*)", powerupHealAmount);
			
			effectBullDuration = (new SMatcher<Integer>()).match(line, "effectBullDuration=(.*)", effectBullDuration);
			effectBurstDuration = (new SMatcher<Integer>()).match(line, "effectBurstDuration=(.*)", effectBurstDuration);
			effectForceBoostDuration = (new SMatcher<Integer>()).match(line, "effectForceBoostDuration=(.*)", effectForceBoostDuration);
			
			entityLife = (new SMatcher<Integer>()).match(line, "entityLife=(.*)", entityLife);
			entityShield = (new SMatcher<Integer>()).match(line, "entityShield=(.*)", entityShield);
			
			bulletDamage = (new SMatcher<Integer>()).match(line, "bulletDamage=(.*)", bulletDamage);
		}
	}
	
	private static class SMatcher<Type>{
		@SuppressWarnings("unchecked")
		public Type match(String string, String pattern, Type defaultValue){
			Matcher matcher;
			Pattern p;
			p = Pattern.compile(pattern);
			matcher = p.matcher(string);
			if(matcher.find()){
				String parameter = matcher.group(1);
				if (defaultValue instanceof Integer){
					try{
						Integer i = Integer.parseInt(parameter);
						return (Type)i;
					} catch (NumberFormatException e){}
				}
				if (defaultValue instanceof Float){
					try{
						Float f = Float.parseFloat(parameter);
						return (Type)f;
					} catch (NumberFormatException e){}
				}
				if (parameter.toLowerCase().equals("true"))
					return (Type)new Boolean(true);
				if (parameter.toLowerCase().equals("false"))
					return (Type)new Boolean(false);
				if (defaultValue instanceof String){
					return (Type)parameter;
				}
				System.out.println("[Warning]Could not decode line: "+string+", it should be "+defaultValue.getClass());
				return defaultValue;
			} else {
				return defaultValue;
			}
		}
	}
	
	
	public static void InitSpecifications(){
		parseDir("res");
		parseConfig();
	}
}
