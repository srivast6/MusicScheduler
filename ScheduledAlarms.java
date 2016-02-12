import java.util.*;

public class ScheduledAlarms{
  
  Timer t;

  //constructor
  public ScheduledAlarms(Alarm x){
  
      t = new Timer();
      Date scheduleFor = new Date();
      scheduleFor = x.getAlarmTimeDateObject();
      System.out.println("Date getting passed: "+scheduleFor.toString());
      t.schedule(new AlarmSet(x),scheduleFor);
      System.out.println("Set Time for: "+x.getAlarmTime());
  }
  
  
  class AlarmSet extends TimerTask{
    
    Alarm alarmSetFor;

    public AlarmSet(Alarm x){
      this.alarmSetFor = x;
    }
    @Override
    public void run(){
      System.out.println(alarmSetFor.getAlarmSound());
      t.cancel();
    }
  }

  public void cancel(){
    t.cancel();
  }

  public static void main (String[] args){
    Alarm alarm1 = new Alarm();
    Alarm alarm2 = new Alarm();
    Alarm alarm3 = new Alarm();
    Alarm alarm4 = new Alarm();

    alarm1.setAlarmTimeWithInts(14,56);
    alarm2.setAlarmTimeWithInts(14,57);
    alarm3.setAlarmTimeWithInts(14,58);
    alarm4.setAlarmTimeWithInts(14,59);
    alarm1.turnAlarmOn();
    alarm2.turnAlarmOn();
    alarm3.turnAlarmOn();
    alarm4.turnAlarmOn();

    ScheduledAlarms test1 = new ScheduledAlarms(alarm1);
    ScheduledAlarms test2 = new ScheduledAlarms(alarm2);
    ScheduledAlarms test3 = new ScheduledAlarms(alarm3);
    ScheduledAlarms test4 = new ScheduledAlarms(alarm4);
    System.out.println("Testing - ");
  
  }

}
