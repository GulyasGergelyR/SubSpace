package testScripts;

import java.nio.ByteBuffer;
import java.util.Locale;

public class TestScript1 {
	public static void main(String[] args) {
		test1();
		test2();
		test3();
		test4();
		long l = Long.parseUnsignedLong("9223372036854775807")*2;
		System.out.println(Long.toUnsignedString(l));
		System.out.println(Integer.signum(-10));
		
		System.out.println(String.format(Locale.ROOT,"%.2f;%.2f", 10f,123.1234f));
		for (int i=0; i<10;i++)
			System.out.println(System.nanoTime());
		int id = 66000;
		byte[] data = ByteBuffer.allocate(2).putShort((short)id).array();
		ByteBuffer buffer = ByteBuffer.wrap(data);
		//buffer.put((byte)10);
		//data[1] = 11;
		System.out.println(buffer.getShort(0));
		for (byte b: data)
			System.out.println(b);
	
		System.out.println((125.52432f-((byte)125.52432f))*10000);
		System.out.println(1<<3);

	}
	private static void test1(){
		System.out.println("-------------------test1-------------------------");
		System.out.println("Should Succeed:"); test11("HElloBello"+new String(new byte[2]));
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
		System.out.println(SMessagePatterns.IsMessageValid(message));
		System.out.println("name: "+SMessagePatterns.getConnectCommandName(message));
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
		System.out.println(SMessagePatterns.IsMessageValid(message));
		System.out.println(SMessagePatterns.getEntityCommandWASD(message));
		System.out.println(SMessagePatterns.getEntityCommandMouse(message));
		System.out.println(SMessagePatterns.getEntityCommandNums(message));
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
		System.out.println(SMessagePatterns.IsMessageValid(message));
		System.out.println(SMessagePatterns.getPingCommandTime(message));
		System.out.println(SMessagePatterns.getPingCommandPrevPing(message));
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
		System.out.println(SMessagePatterns.IsMessageValid(message));
		System.out.println("posx: "+SMessagePatterns.getPosX(message));
		System.out.println("posy: "+SMessagePatterns.getPosY(message));
		System.out.println("lookdirx: "+SMessagePatterns.getLookDirX(message));
		System.out.println("lookdiry: "+SMessagePatterns.getLookDirY(message));
		System.out.println("movedirx: "+SMessagePatterns.getMoveDirX(message));
		System.out.println("movediry: "+SMessagePatterns.getMoveDirY(message));
		System.out.println("content: "+SMessagePatterns.getContent(message));
		System.out.println("-------------------------------------------------\n");
	}
}
