import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class PlaylistScheduleGUI {

  // <<<<<<< HEAD
  // MainFrame
  private JFrame scheduleFrame;
  private JPanel schedulePanel;
  private JPanel topPanel;
  private JButton scheduleButton;
  private JList playlistView;
  private JSpinner timeSpinner;
  private ArrayList<String> playlistEntries;

  ScheduledPlay scheduler;
  private MusicHome musicHome;
  public String[] playlistNames;

  int selectedIndex;
  String playlistToSchedule; // stores name of playlist to be played
  Date d; // stores date at which the playlist should be played

  DateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

  public PlaylistScheduleGUI(MusicHome m, String[] playlistNames) {
    this.musicHome = m;
    this.playlistNames = playlistNames;
  }

  public void prepareGUI() {
    scheduleFrame = new JFrame("Schedule a Playlist");
    scheduleFrame.setSize(300, 200);
    scheduleFrame.setLayout(new BorderLayout());
    scheduleFrame.setMinimumSize(new Dimension(300, 200));

    scheduleFrame.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent evt) {
        Component c = (Component) evt.getSource();

        System.out.println(c.getBounds());
      }

      public void componentMoved(ComponentEvent evt) {
        Component c = (Component) evt.getSource();
        System.out.println(c.getBounds());
      }

    });
    addschedulePanel();
    scheduleFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        scheduleFrame.setVisible(false);
      }
    });
    scheduleFrame.setVisible(true);
  }



  private void addschedulePanel() {
    schedulePanel = new JPanel(new BorderLayout());
    addPlaylistView();
    addScheduleButton();
    scheduleFrame.add(schedulePanel, BorderLayout.CENTER);
  }

  private void addScheduleButton() {
    // TODO Auto-generated method stub
    scheduleButton = new JButton("Schedule Playlist");
    scheduleButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        //d = (Date) timeSpinner.getValue();
        Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,5);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		d = c.getTime();
        playlistToSchedule = playlistNames[selectedIndex];
        System.out.println("schedule " + playlistToSchedule + " at " + formatter.format(d));
        writeSchduleToFile(playlistToSchedule, d);

        // Constuctor creates the timer thread. nothing else is neccessary.
        // This object should be saved if the timer needs to be canceled.
        scheduler = new ScheduledPlay(d, new Playlist(playlistToSchedule), musicHome);

        // readSchdule();
      }
    });
    schedulePanel.add(scheduleButton, BorderLayout.SOUTH);
  }

  private void addPlaylistView() {
    // TODO Auto-generated method stub
    topPanel = new JPanel(new BorderLayout());
    // making playlist view
    playlistEntries = new ArrayList<>();
	ArrayList<String> buggyList = new ArrayList<>();
	System.out.println("Size" + playlistNames.length);
	for(int i = 0; i < 2 && i < playlistNames.length; i++) {
		buggyList.add(playlistNames[i]);
	}
	playlistView = new JList<>(buggyList.toArray());
    //playlistView = new JList<>(playlistNames);
    
    playlistView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    playlistView.setSelectedIndex(0);
    playlistView.addListSelectionListener(new ListSelectionListener() {

      @Override
      public void valueChanged(ListSelectionEvent e) {
        // TODO Auto-generated method stub
        selectedIndex = playlistView.getSelectedIndex();
      }
    });
    // making time and date
    timeSpinner = new JSpinner();
    timeSpinner.setModel(new SpinnerDateModel());
    timeSpinner.setSize(100, playlistView.getHeight());
    timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "yyyy-MMM-dd HH:mm:ss"));

    // add to top panel
    topPanel.add(playlistView, BorderLayout.CENTER);
    topPanel.add(timeSpinner, BorderLayout.NORTH);
    schedulePanel.add(topPanel, BorderLayout.CENTER);
  }



  public String getPlaylistToSchedule() {
    return playlistToSchedule;
  }

  public Date getDate() {
    return d;
  }

  private void writeSchduleToFile(String name, Date date) {
    try {
      File file = new File("playlistTimings.txt");
      if (!file.exists()) {
        file.createNewFile();
      }
      FileWriter fw = new FileWriter(file, true);
      // BufferedWriter writer give better performance
      BufferedWriter bw = new BufferedWriter(fw);
      String writeToFile = name + "\n" + formatter.format(date) + "\n";
      bw.write(writeToFile);
      System.out.print("Writing " + writeToFile);
      // Closing BufferedWriter Stream
      bw.close();
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
  }

  public void readSchdule() {
    File file = new File("playlistTimings.txt");
    String name;
    Date date;
    System.out.println("Reading");
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String l;
      while ((l = br.readLine()) != null) {
        name = l;
        date = (Date) formatter.parse(br.readLine());
        System.out.println(name + " " + formatter.format(date));
      }
      br.close();
    } catch (IOException ioe) {
      System.out.println(ioe);
    } catch (ParseException e) {
      // TODO: handle exception
      System.out.println(e);
    }

  }
  /*
   * public static void main(String[] args) { PlaylistScheduleGUI p = new PlaylistScheduleGUI();
   * p.prepareGUI(); }
   */

  /*
   * ======= // scheduleFrame private JFrame scheduleFrame; private JPanel schedulePanel; private
   * JPanel topPanel; private JButton scheduleButton; private JList playlistView; private JSpinner
   * timeSpinner; private ArrayList<String> playlistEntries; int selectedIndex; String
   * playlistToSchedule; // stores name of playlist to be played Date d; // stores date at which the
   * playlist should be played
   * 
   * DateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
   * 
   * public PlaylistScheduleGUI(ArrayList<String> playlistEntries) { this.playlistEntries =
   * playlistEntries;
   * 
   * }
   * 
   * public void prepareScheduleGUI() { scheduleFrame = new JFrame("Schedule a Playlist");
   * scheduleFrame.setSize(300, 200); scheduleFrame.setLayout(new BorderLayout());
   * scheduleFrame.setMinimumSize(new Dimension(300, 200));
   * 
   * scheduleFrame.addComponentListener(new ComponentAdapter() { public void
   * componentResized(ComponentEvent evt) { Component c = (Component) evt.getSource();
   * 
   * System.out.println(c.getBounds()); }
   * 
   * public void componentMoved(ComponentEvent evt) { Component c = (Component) evt.getSource();
   * System.out.println(c.getBounds()); }
   * 
   * }); addschedulePanel(); scheduleFrame.addWindowListener(new WindowAdapter() { public void
   * windowClosing(WindowEvent windowEvent) { scheduleFrame.setVisible(false); } });
   * scheduleFrame.setVisible(true); }
   * 
   * private void addschedulePanel() { schedulePanel = new JPanel(new BorderLayout());
   * addPlaylistView(); addScheduleButton(); scheduleFrame.add(schedulePanel, BorderLayout.CENTER);
   * }
   * 
   * private void addScheduleButton() { // TODO Auto-generated method stub scheduleButton = new
   * JButton("Schedule Playlist"); scheduleButton.addActionListener(new ActionListener() {
   * 
   * @Override public void actionPerformed(ActionEvent e) { // TODO Auto-generated method stub d =
   * (Date) timeSpinner.getValue(); playlistToSchedule = playlistEntries.get(selectedIndex);
   * System.out.println("schedule " + playlistToSchedule + " at " + formatter.format(d));
   * writeSchduleToFile(playlistToSchedule, d); readSchdule(); } });
   * schedulePanel.add(scheduleButton, BorderLayout.SOUTH); }
   * 
   * private void addPlaylistView() { // TODO Auto-generated method stub topPanel = new JPanel(new
   * BorderLayout()); // making playlist view
   * 
   * playlistView = new JList<>(playlistEntries.toArray());
   * playlistView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   * playlistView.setSelectedIndex(0); playlistView.addListSelectionListener(new
   * ListSelectionListener() {
   * 
   * @Override public void valueChanged(ListSelectionEvent e) { // TODO Auto-generated method stub
   * selectedIndex = playlistView.getSelectedIndex(); } }); // making time and date timeSpinner =
   * new JSpinner(); timeSpinner.setModel(new SpinnerDateModel()); timeSpinner.setSize(100,
   * playlistView.getHeight()); timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner,
   * "yyyy-MMM-dd HH:mm:ss"));
   * 
   * // add to top panel topPanel.add(playlistView, BorderLayout.CENTER); topPanel.add(timeSpinner,
   * BorderLayout.NORTH); schedulePanel.add(topPanel, BorderLayout.CENTER); }
   * 
   * 
   * public String getPlaylistToSchedule() { return playlistToSchedule; }
   * 
   * public Date getDate() { return d; }
   * 
   * private void writeSchduleToFile(String name, Date date) { try { File file = new
   * File("playlistTimings.txt"); if (!file.exists()) { file.createNewFile(); } FileWriter fw = new
   * FileWriter(file, true); // BufferedWriter writer give better performance BufferedWriter bw =
   * new BufferedWriter(fw); String writeToFile = name + "\n" + formatter.format(date) + "\n";
   * bw.write(writeToFile); System.out.print("Writing " + writeToFile); // Closing BufferedWriter
   * Stream bw.close(); } catch (IOException ioe) { System.out.println(ioe); } }
   * 
   * public void readSchdule() { File file = new File("playlistTimings.txt"); String name; Date
   * date; System.out.println("Reading"); try { BufferedReader br = new BufferedReader(new
   * FileReader(file)); String l; while ((l = br.readLine()) != null) { name = l; date = (Date)
   * formatter.parse(br.readLine()); System.out.println(name + " " + formatter.format(date)); }
   * br.close(); } catch (IOException ioe) { System.out.println(ioe); } catch (ParseException e) {
   * // TODO: handle exception System.out.println(e); } }
   */
}
