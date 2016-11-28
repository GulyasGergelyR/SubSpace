package GameEngine.SyncEngine;

import GameEngine.Specifications;

public class SServerTimer {
	private long TS; //The time when the cycle begun
    
    public SServerTimer(){
    	StartTimer();
    }
    
    public void StartTimer(){
    	TS=System.nanoTime();
    }
    
    public static long GetNanoTime(){
    	return System.nanoTime();
    }
    
    public void SleepIfRequired(){
    	//Calculate how long did the cycle take
    	long l = 1000/Specifications.FPS_M*1000*1000; 
    	long TC=System.nanoTime();
		long time=l-(TC-TS);
		if(time>0)
		{
			try {
				long mili=time/1000000;
				int nano=(int)time%1000000;
				Thread.sleep(mili,nano);
    		} catch(InterruptedException ex) {
    		    Thread.currentThread().interrupt();
    		}
		}
    }
}
