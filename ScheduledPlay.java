import java.util.*;

public class ScheduledPlay {
	
	Timer t;
	
	ScheduledPlay ( Date time ) {
		t = new Timer();
		t.schedule(/*TimerTask*/ task, time);
	}
	
	
}
