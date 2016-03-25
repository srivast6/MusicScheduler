import javax.swing.JOptionPane;

public class PromptBox {

    public static int snoozeBox(String alarmTime)
    {
    	Object[] options = {"Snooze",
                "Turn OFF!"};
       int n = JOptionPane.showOptionDialog(null, alarmTime, "Alarm",
        	    JOptionPane.YES_NO_OPTION,
        	    JOptionPane.QUESTION_MESSAGE,
        	    null,     //do not use a custom Icon
        	    options,  //the titles of buttons
        	    options[0]);
        
        return n;
    }
    
    public static int snoozeLengthPicker()
    {
    	Object[] possibilities = {"1","2","3","4","5","6","7","8","9","10"};
       String n = (String) JOptionPane.showInputDialog(null, "Your snooze length",
    		   null, 
    		   JOptionPane.PLAIN_MESSAGE,
               null,
               possibilities,
               possibilities[0]);
        
        return Integer.parseInt(n);
    }

}