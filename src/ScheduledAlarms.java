import java.io.File;
import java.util.*;

import javax.swing.JOptionPane;

public class ScheduledAlarms{


  Timer t;
  MusicPlayer player;
  Alarm alarmScheduled;

  //constructor
  public ScheduledAlarms(Alarm x, MusicPlayer player){
  
      this.t = new Timer();
      this.player = player;
      this.alarmScheduled = x;
      Calendar scheduleForCal = new GregorianCalendar();
      scheduleForCal = x.getAlarmTimeCalendarObjectClean();
      System.out.println("Date getting passed: "+scheduleForCal.getTime());
      t.schedule(new AlarmSet(x, player),(Date) scheduleForCal.getTime());
      System.out.println("Set Alarm for: "+x.getAlarmTime());
  }
  
  
  class AlarmSet extends TimerTask{
    
	  Alarm currentScheduledAlarm;
	  MusicPlayer currentPlayer;
	  
	  AlarmSet(Alarm alarmScheduled, MusicPlayer player){
		this.currentScheduledAlarm = alarmScheduled;
		this.currentPlayer = player;
	  }
 
    @Override
    public void run(){
      System.out.println("Going off Alarm Time = "+currentScheduledAlarm.getAlarmTime()+" Alarm Sound = "+currentScheduledAlarm.getAlarmSound());
      currentPlayer.setSong(currentScheduledAlarm.getAlarmSound());
      if(currentPlayer.isPlaying()){
    	  currentPlayer.stop();
    	  currentPlayer.play(-1); 
      }
      else
    	  currentPlayer.play(-1); 
      
      int promptReturn = PromptBox.snoozeBox(currentScheduledAlarm.getAlarmTime());
      if(promptReturn == 0){//Snooze
    	  currentPlayer.pause();
    	  snooze(currentScheduledAlarm.getSnoozeLengthInMinutes());
      }
      else{
    	  currentPlayer.pause();
	  	  cancelTimer();
      }
      
    }
    
    public void snooze(int minutes){
    	System.out.println("yayy");
    	alarmScheduled.addMinute(minutes);
    	t.cancel();
    	t = new Timer();
    	Calendar scheduleForCal2 = new GregorianCalendar();
        scheduleForCal2 = alarmScheduled.getAlarmTimeCalendarObjectClean();
    	System.out.println("Date getting passed: "+alarmScheduled.getAlarmTimeCalendarObjectClean().getTime());
    	t.schedule(new AlarmSet(alarmScheduled, player),(Date) scheduleForCal2.getTime());
        System.out.println("Set Alarm for: "+alarmScheduled.getAlarmTime());
        
    }
    
    public void cancelTimer(){
    	t.cancel();
    }
  }

 

  public static void main (String[] args){
	  
	  BooleanChangeListener listener = new BooleanChangeListener() {
	      @Override
	      public void stateChanged(BooleanChangeEvent event) {
	        System.out.println("Detected change to: " + event.getDispatcher().getFlag() + " -- event: "
	            + event);
	      }
	    };
	BooleanEventListener isPlaying = new BooleanEventListener(false);
	isPlaying.addBooleanChangeListener(listener);
	
    Alarm alarm1 = new Alarm();
    Alarm alarm2 = new Alarm();
    Alarm alarm3 = new Alarm();
    Alarm alarm4 = new Alarm();

    //alarm1.setAlarmTimeWithInts(23,36);
   // alarm2.setAlarmTimeWithInts(23,37);
   // alarm3.setAlarmTimeWithInts(23,38);
    alarm4.setAlarmTimeWithInts(13,33);
   // alarm1.turnAlarmOn();
   // alarm2.turnAlarmOn();
   // alarm3.turnAlarmOn();
    alarm4.turnAlarmOn();

  //  ScheduledAlarms test1 = new ScheduledAlarms(alarm1);
   // ScheduledAlarms test2 = new ScheduledAlarms(alarm2);
   // ScheduledAlarms test3 = new ScheduledAlarms(alarm3);
    MusicPlayer playing = new MusicPlayer(isPlaying);
    playing.selectDirectory();
    ScheduledAlarms test4 = new ScheduledAlarms(alarm4, playing);
    System.out.println("Testing - ");
  
  }

}

/*import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledAlarms{
  
  Timer t;

  //constructor
  public ScheduledAlarms(Alarm x){
  
      t = new Timer();
      Calendar scheduleForCal = new GregorianCalendar();
      scheduleForCal = x.getAlarmTimeCalendarObject();
      System.out.println("Date getting passed: "+scheduleForCal.getTime());
      t.schedule(new AlarmSet(),(Date) scheduleForCal.getTime());
      System.out.println("Set Time for: "+x.getAlarmTime());
  }
  
  
  class AlarmSet extends TimerTask{
    
 
    @Override
    public void run(){
      System.out.println("BEEEP");
    
    }
  }

 

  public static void main (String[] args){
    Alarm alarm1 = new Alarm();
    Alarm alarm2 = new Alarm();
    Alarm alarm3 = new Alarm();
    Alarm alarm4 = new Alarm();

    //alarm1.setAlarmTimeWithInts(23,36);
   // alarm2.setAlarmTimeWithInts(23,37);
   // alarm3.setAlarmTimeWithInts(23,38);
    alarm4.setAlarmTimeWithInts(19,6);
   // alarm1.turnAlarmOn();
   // alarm2.turnAlarmOn();
   // alarm3.turnAlarmOn();
    alarm4.turnAlarmOn();

  //  ScheduledAlarms test1 = new ScheduledAlarms(alarm1);
   // ScheduledAlarms test2 = new ScheduledAlarms(alarm2);
   // ScheduledAlarms test3 = new ScheduledAlarms(alarm3);
    //ScheduledAlarms test4 = new ScheduledAlarms(alarm4);
    System.out.println("Testing - ");
    
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
   
    alarm4.getAlarmTimeCalendarObject();
    System.out.println("The time is : " + new Date());
    System.out.println("Date getting passed: "+alarm4.getAlarmTimeDateObject());
    
    executor.scheduleWithFixedDelay(alarm4, alarm4.getAlarmTimeCalendarObject().getTimeInMillis(), 60000 , TimeUnit.MILLISECONDS);
    //executor.schedule(task2, 10 , TimeUnit.SECONDS);
     
    try {
          executor.awaitTermination(1, TimeUnit.DAYS);
    } catch (InterruptedException e) {
          e.printStackTrace();
    }
     
    executor.shutdown();
  
  }*/

