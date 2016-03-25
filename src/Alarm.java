import java.io.File;
import java.util.*;

public class Alarm {

  private String name;
  private Calendar alarmTime;
  private boolean onOff;
  private int alarmHour;
  private int alarmMinute;
  private File alarmSound;
  private int snoozeLength;

    //default constructor
    public Alarm(){
      this.name = "";
      this.alarmTime = new GregorianCalendar();
      this.alarmHour = alarmTime.get(Calendar.HOUR_OF_DAY);
      this.alarmMinute = alarmTime.get(Calendar.MINUTE);
      this.onOff = false;
      this.snoozeLength = 1;
    }

  public Alarm(String nameOfAlarm, Calendar time, boolean alarmOnOrOff){
    this.name = nameOfAlarm;
    this.alarmTime = time;
    this.onOff = alarmOnOrOff;
  }

  //getters and setters

  public void setName(String x){
    this.name = x;
  }

  public String getName(){
    return this.name;
  }

  public void setAlarmTime(Calendar x){
    this.alarmTime = x;
    this.alarmHour = alarmTime.get(Calendar.HOUR_OF_DAY);
    this.alarmMinute = alarmTime.get(Calendar.MINUTE);
  }

  public void setAlarmTimeWithInts(int hour, int minutes){
    this.alarmTime.set(Calendar.HOUR_OF_DAY, hour);
    this.alarmTime.set(Calendar.MINUTE, minutes);
    this.alarmTime.set(Calendar.SECOND, 0);
    this.alarmTime.set(Calendar.MILLISECOND, 0);
    this.alarmHour = alarmTime.get(Calendar.HOUR_OF_DAY);
    this.alarmMinute = alarmTime.get(Calendar.MINUTE);
  }

  public String getAlarmTime(){
    return alarmTime.getTime().toString();
  }

  //THIS FORCES CORRECT DATE
  public Date getAlarmTimeDateObject(){
    Calendar today = Calendar.getInstance();
    int todayYear = today.get(Calendar.YEAR);
    int todayMonth = today.get(Calendar.MONTH);
    int todayDate = today.get(Calendar.DAY_OF_MONTH);
    this.alarmTime.set(Calendar.YEAR,todayYear);
    this.alarmTime.set(Calendar.MONTH, todayMonth);
    this.alarmTime.set(Calendar.DAY_OF_MONTH, todayDate);
    Date dateObject = new Date();
    dateObject.setHours(this.alarmHour);
    dateObject.setMinutes(this.alarmMinute);
    return dateObject;
  }
  
//THIS FORCES CORRECT DATE
 public Calendar getAlarmTimeCalendarObject(){
	  Calendar today = Calendar.getInstance();
	  int todayYear = today.get(Calendar.YEAR);
	  int todayMonth = today.get(Calendar.MONTH);
	  int todayDate = today.get(Calendar.DAY_OF_MONTH);
	  this.alarmTime.set(Calendar.YEAR,todayYear);
	  this.alarmTime.set(Calendar.MONTH, todayMonth);
	  this.alarmTime.set(Calendar.DAY_OF_MONTH, todayDate);
	  
	  return this.alarmTime;
}
 
 public Calendar getAlarmTimeCalendarObjectClean(){	  
	  return this.alarmTime;
}
  
  public void addMinute(int minuteToAdd){
	  alarmTime.add(Calendar.MINUTE, minuteToAdd);
  }
  
  public void addDay(int numDays){
	  alarmTime.add(Calendar.DATE, numDays);
  }

  public int getHour(){
    return this.alarmHour;
  } 
  public int getMinute(){
    return this.alarmMinute;
  }
  public void turnAlarmOn(){
    this.onOff = true;
  }
  public void turnAlarmOff(){
    this.onOff = false;
  }
  public boolean isAlarmOn(){
    return this.onOff;
  }
  public void setAlarmSound(File x){
    this.alarmSound = x;
  }
  public File getAlarmSound(){
    return this.alarmSound;
  }
  
  public void setSnoozeLengthInMinutes(int minutes){
	  this.snoozeLength = minutes;
  }
  
  public int getSnoozeLengthInMinutes(){
	  return this.snoozeLength;
  }


    public static void main(String[] args){
      Alarm test1 = new Alarm();
      test1.setAlarmTimeWithInts(22,4);
      System.out.println("Time returned = "+test1.getAlarmTime());
      System.out.println("Time returned = "+test1.getHour());
      System.out.println("Time returned = "+test1.getMinute());
    }
}