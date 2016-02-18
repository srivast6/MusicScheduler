import java.util.*;

public class Alarm{

  private String name;
  private Calendar alarmTime;
  private boolean onOff;
  private int alarmHour;
  private int alarmMinute;
  private String alarmSound;

    //default constructor
    public Alarm(){
      this.name = "";
      this.alarmTime = new GregorianCalendar();
      this.alarmHour = alarmTime.get(Calendar.HOUR_OF_DAY);
      this.alarmMinute = alarmTime.get(Calendar.MINUTE);
      this.onOff = false;
      this.alarmSound = "BEEEEEEEEEP";
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
    this.alarmTime.set(Calendar.HOUR, hour);
    this.alarmTime.set(Calendar.MINUTE, minutes);
    this.alarmHour = alarmTime.get(Calendar.HOUR_OF_DAY);
    this.alarmMinute = alarmTime.get(Calendar.MINUTE);
  }

  public String getAlarmTime(){
    return alarmTime.getTime().toString();
  }

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
  public void setAlarmSound(String x){
    this.alarmSound = x;
  }
  public String getAlarmSound(){
    return this.alarmSound;
  }

    public static void main(String[] args){
      Alarm test1 = new Alarm();
      test1.setAlarmTimeWithInts(22,4);
      System.out.println("Time returned = "+test1.getAlarmTime());
      System.out.println("Time returned = "+test1.getHour());
      System.out.println("Time returned = "+test1.getMinute());
    }
}
