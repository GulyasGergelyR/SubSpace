package testScripts;

import java.nio.ByteBuffer;
import java.util.Locale;

import GameEngine.Specifications;
import GameEngine.GeomEngine.SVector;

public class TestScript1 {
	public static void main(String[] args) {
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
		System.out.println(1000/Specifications.FPS_M*1000*1000);
		
		int currentLife = 3;
		
		SVector leftBottom = new SVector(1-((currentLife-1)/5+1)*0.2f, ((currentLife-1)%5)*0.2f);
		SVector rightUpper = new SVector(1-((currentLife-1)/5)*0.2f, ((currentLife-1)%5+1)*0.2f);
		System.out.println(leftBottom.getString());
		System.out.println(rightUpper.getString());

	}
}
