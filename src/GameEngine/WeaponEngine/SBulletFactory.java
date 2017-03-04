package GameEngine.WeaponEngine;

import GameEngine.SId;
import GameEngine.GeomEngine.SVector;
import GameEngine.ObjectEngine.SFactory;
import Main.SMain;
import WebEngine.MessageEngine.SM;
import WebEngine.MessageEngine.SMPatterns;

public class SBulletFactory extends SFactory<SBullet> {
	public static byte simpleBullet = 21;
	public SBulletFactory(){
		super("Bullet factory", (byte)20);
	}
	
	public void createNewBulletAtClient(int ownerId,int id, SVector pos,SVector lookdir,SVector movedir, byte bulletType){
		if (bulletType == simpleBullet){
			SBullet bullet = new SBullet(ownerId, pos, lookdir, movedir);
			bullet.setId(new SId(id));
			addObject(bullet);
		}
	}
	public void createNewBulletAtServer(SBullet bullet){
		addObject(bullet);
		SM message = SMPatterns.getObjectCreateMessage(bullet);
		SMain.getCommunicationHandler().SendMessage(message);
	}
}
