package testScripts;

import WebEngine.ComEngine.SMessage;
import WebEngine.ComEngine.SMessageParser;

public class TestScript1 {
	public static void main(String[] args) {
		test1();
		test2();
		test3();
		test4();
		long l = Long.parseUnsignedLong("9223372036854775807")*2;
		System.out.println(Long.toUnsignedString(l));
	}
	private static void test1(){
		System.out.println("-------------------test1-------------------------");
		System.out.println("Should Succeed:"); test11("HElloBello");
		System.out.println("Should Fail: (Too short)"); test11("HEll");
		System.out.println("Should Fail: (Too long)"); test11("HElloooooooooooooooooooooooo");
		System.out.println("Should Fail: (wrong character)"); test11("HElloBe111");
		System.out.println("------------------test1end------------------------\n");
	}
	private static void test11(String S){
		System.out.println("-----test11-------");
		String testId = "06732ac0-51c6-4ba1-a45e-41e82d107847";
		String testCommand = "CNNCL";
		String testName = S;
		String testInput = testId+";"+testCommand+";"+testName+";";
		SMessage message = new SMessage(testInput.getBytes());
		System.out.println(testInput);
		System.out.println(SMessageParser.IsMessageValid(message));
		System.out.println("name: "+SMessageParser.getConnectCommandName(message));
		System.out.println("------------------\n");
	}
	private static void test2(){
		System.out.println("-------------------test2-------------------------");
		String testId = "06732ac0-51c6-4ba1-a45e-41e82d107847";
		String testCommand = "ENTIN";
		String testContent = "PA;PD;PW;PML;PS;K4;K9;";
		String testInput = testId+";"+testCommand+";"+testContent;
		SMessage message = new SMessage(testInput.getBytes());
		System.out.println(testInput);
		System.out.println(SMessageParser.IsMessageValid(message));
		System.out.println(SMessageParser.getEntityCommandWASD(message));
		System.out.println(SMessageParser.getEntityCommandMouse(message));
		System.out.println(SMessageParser.getEntityCommandNums(message));
		System.out.println("-------------------------------------------------\n");
	}
	private static void test3(){
		System.out.println("-------------------test3-------------------------");
		String testId = "06732ac0-51c6-4ba1-a45e-41e82d107847";
		String testCommand = "PNGRQ";
		String testContent = "12312312;";
		String testInput = testId+";"+testCommand+";"+testContent;
		SMessage message = new SMessage(testInput.getBytes());
		System.out.println(testInput);
		System.out.println(SMessageParser.IsMessageValid(message));
		System.out.println(SMessageParser.getPingCommandTime(message));
		System.out.println(SMessageParser.getPingCommandPrevPing(message));
		System.out.println("-------------------------------------------------\n");
	}
	private static void test4(){
		System.out.println("-------------------test4-------------------------");
		String testId = "06732ac0-51c6-4ba1-a45e-41e82d107847";
		String testCommand = "ENTUP";
		String testContent = "p;-123.23;3322.0;ld;0.0;-1.0;md;0.3;0.67;";
		String testInput = testId+";"+testCommand+";"+testContent;
		SMessage message = new SMessage(testInput.getBytes());
		System.out.println(testInput);
		System.out.println(SMessageParser.IsMessageValid(message));
		System.out.println("posx: "+SMessageParser.getPosX(message));
		System.out.println("posy: "+SMessageParser.getPosY(message));
		System.out.println("lookdirx: "+SMessageParser.getLookDirX(message));
		System.out.println("lookdiry: "+SMessageParser.getLookDirY(message));
		System.out.println("movedirx: "+SMessageParser.getMoveDirX(message));
		System.out.println("movediry: "+SMessageParser.getMoveDirY(message));
		System.out.println("content: "+SMessageParser.getContent(message));
		System.out.println("-------------------------------------------------\n");
	}
}
