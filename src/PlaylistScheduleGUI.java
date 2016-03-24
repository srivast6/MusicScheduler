import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BoxLayout;
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

	//MainFrame
	private JFrame mainframe;
	private JPanel mainPanel;
	private JPanel topPanel;
	private JButton scheduleButton;
	private JList playlistView;
	private JSpinner timeSpinner;
	private ArrayList<String> playlistEntries;
	int selectedIndex;
	String playlistToSchedule; //stores name of playlist to be played
	Date d;						//stores date at which the playlist should be played
	public PlaylistScheduleGUI() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void prepareGUI() {
		mainframe = new JFrame("Schedule a Playlist");
		mainframe.setSize(300, 200);
	    mainframe.setLayout(new BorderLayout());
	    mainframe.setMinimumSize(new Dimension(300, 200));

	    mainframe.addComponentListener(new ComponentAdapter() {
	      public void componentResized(ComponentEvent evt) {
	        Component c = (Component) evt.getSource();

	        System.out.println(c.getBounds());
	      }

	      public void componentMoved(ComponentEvent evt) {
	        Component c = (Component) evt.getSource();
	        System.out.println(c.getBounds());
	      }

	    });
	    addMainPanel();
	    mainframe.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent windowEvent) {
	    	  mainframe.setVisible(false);
	      }
	    });
	    mainframe.setVisible(true);
	}
	
	private void addMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
	    addPlaylistView();
	    addScheduleButton();
	    mainframe.add(mainPanel);
	}
	
	private void addScheduleButton() {
		// TODO Auto-generated method stub
		scheduleButton = new JButton("Schedule Playlist");
		scheduleButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				d = (Date) timeSpinner.getValue();
				playlistToSchedule = playlistEntries.get(selectedIndex);
				System.out.println("schedule " + playlistToSchedule + " at " + d);
			}
		});
		mainPanel.add(scheduleButton);
	}

	private void addPlaylistView() {
		// TODO Auto-generated method stub
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
		//making playlist view
		playlistEntries = new ArrayList<>();
		playlistEntries = getPlaylistEntries();
		playlistView = new JList<>(getPlaylistEntries().toArray());
		playlistView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    playlistView.setSelectedIndex(0);
	    playlistView.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				selectedIndex = playlistView.getSelectedIndex();
			}
		});
	    //making time and date
	    timeSpinner = new JSpinner();
	    timeSpinner.setModel(new SpinnerDateModel());
	    timeSpinner.setSize(100, playlistView.getHeight());
	    timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "yyyy-MMM-dd HH:mm:ss"));
	    
	    //add to top panel
	    topPanel.add(playlistView);
	    topPanel.add(timeSpinner);
	    mainPanel.add(topPanel);
	}
	
	

	private ArrayList<String> getPlaylistEntries() {
		ArrayList<String> s = new ArrayList<>();
		String[] playlistNames = new MusicHome().getPlaylistDirectory();
		for(int i = 0; i < playlistNames.length; i++) {
			s.add(playlistNames[i]);
		}
		return s;
	}
	
	public String getPlaylistToSchedule() {
		return playlistToSchedule;
	}
	
	public Date getDate() {
		return d;
	}

	/*
	public static void main(String[] args) {
	     PlaylistScheduleGUI p = new PlaylistScheduleGUI();
	     p.prepareGUI();
	  }
	*/
}
