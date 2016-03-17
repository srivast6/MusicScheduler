import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.glass.events.KeyEvent;


/**
 * Created by gauravsrivastava on 2/6/16.
 */
public class MusicHome {

  // JFrames
  private JFrame mainframe;

  // Panels
  private JPanel controlPanel;
  private JPanel musicControlPanel;
  private JPanel nowPlayingPanel;
  private JPanel mainPanel;
  private JPanel queuePanel;

  // Buttons
  private JButton play;
  private JButton pause;
  private JButton dir;
  private JButton showQueue;

  // Labels
  private JLabel heading;
  private JLabel nowPlaying;
  private JLabel queueTitle;
  private JList playlist;
  private JList songlist;
  private JList queuelist;
  private boolean viewQueue;

  // Lists
  private ArrayList<File> songQueue;
  private ArrayList<String> playlistEntries;
  private DefaultListModel<String> listmodel;
  private DefaultListModel<String> queuemodel;
  private String[] playlistNames;


  // Music Player Requirements
  private String songName;
  private MusicPlayer player;
  private File musicDirectory;


  // Images
  private ImageIcon pauseIcon;
  private ImageIcon playIcon;
  private ImageIcon stopIcon;
  private ImageIcon leftIcon;
  private ImageIcon rightIcon;

  // Required for event and change listeners
  private BooleanChangeListener listener;
  private BooleanEventListener isPlaying;


  // Indexes
  int selectedIndex = 0;
  int maxIndex = 0;

  /*
   * Startup Constructor
   */

  public MusicHome() {

    songQueue = new ArrayList<File>();
    viewQueue = false;
    loadProfile();
    prepareGUI();
    prepareEventListener();
    player = new MusicPlayer(isPlaying);
  }

  public void prepareEventListener() {
    // This event is caused by a song ending or a song no longer playing.
    listener = new BooleanChangeListener() {
      @Override
      public void stateChanged(BooleanChangeEvent event) {
        /*
         * System.out.println("Detected change to: " + event.getDispatcher().getFlag() +
         * " -- event: " + event);
         */

        // UI Changes
        if (event.getDispatcher().getFlag()) {

          heading.setVisible(true);
          nowPlaying.setText(songName);
          pause.setVisible(true);
          play.setIcon(stopIcon);
        } else {

          // play next song in list attempt
          System.out.println("MAX: " + maxIndex);
          System.out.println("CURRENT: " + songlist.getSelectedIndex());
          if (maxIndex > songlist.getSelectedIndex()) {
            songlist.setSelectedIndex(songlist.getSelectedIndex() + 1);
            player.play(-1);
          }

          heading.setVisible(false);
          nowPlaying.setText("");
          pause.setVisible(false);
          pause.setIcon(pauseIcon);
          play.setIcon(playIcon);
        }
      }
    };
    isPlaying = new BooleanEventListener(false);
    isPlaying.addBooleanChangeListener(listener);
  }

  public static void main(String[] args) {
    MusicHome musicHome = new MusicHome();
  }

  private void prepareGUI() {
    try {
      // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

      pauseIcon = new ImageIcon(ImageIO.read(new File("./images/pause.png")));
      playIcon = new ImageIcon(ImageIO.read(new File("./images/play.png")));
      stopIcon = new ImageIcon(ImageIO.read(new File("./images/stop.png")));
      leftIcon = new ImageIcon(ImageIO.read(new File("./images/left.png")));
      rightIcon = new ImageIcon(ImageIO.read(new File("./images/right.png")));

    } catch (Exception e) {
      e.printStackTrace();
    }

    mainframe = new JFrame("Beefed Up Music Player");
    mainframe.setSize(400, 400);
    mainframe.setLayout(new BorderLayout());
    mainframe.setMinimumSize(new Dimension(400, 400));

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
        System.exit(0);
      }
    });

    mainframe.setVisible(true);
  }

  private void addMainPanel() {
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setSize(400, 400);
    addControlPanel();
    addPlaylistPanel();
    addSongListPanel();
    createMenuBar();
    mainframe.add(mainPanel, BorderLayout.CENTER);

  }

  private void refreshQueue() {
    mainframe.remove(queuePanel);
    queueControl();
    mainframe.revalidate();
    mainframe.repaint();
  }


  // used primarily to update view when changing directories
  private void refreshView() {
    mainframe.remove(mainPanel);
    addMainPanel();
    mainframe.revalidate();
    mainframe.repaint();
  }

  private void musicControl() {

    musicControlPanel = new JPanel();
    musicControlPanel.setSize(400, 200);

    play = new JButton(playIcon);
    play.setBorder(BorderFactory.createEmptyBorder());
    play.setContentAreaFilled(false);

    play.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (player.songSelected()) {
          // Play the song
          if (!player.isPlaying()) {
            player.play(-1);
          }
          // Stop the song
          else {
            player.stop();
          }
        } else {
          JOptionPane.showMessageDialog(null, "Select a song to play");
        }
      }
    });

    pause = new JButton(pauseIcon);
    pause.setBorder(BorderFactory.createEmptyBorder());
    pause.setContentAreaFilled(false);

    pause.setVisible(false);
    pause.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!player.isPaused()) {
          pause.setIcon(playIcon);
          player.pause();
        } else {
          pause.setIcon(pauseIcon);
          player.resume(player.song);
        }
      }
    });

    musicControlPanel.add(play);
    musicControlPanel.add(pause);
  }

  private void currentSong() {
    nowPlayingPanel = new JPanel(new BorderLayout());
    nowPlayingPanel.setSize(400, 200);
    heading = new JLabel("Now Playing");
    heading.setVisible(false);

    nowPlaying = new JLabel("");
    nowPlayingPanel.add(heading, BorderLayout.NORTH);
    nowPlayingPanel.add(nowPlaying, BorderLayout.CENTER);
  }

  private void addControlPanel() {
    musicControl();
    currentSong();
    queueControl();
    controlPanel = new JPanel(new BorderLayout());
    controlPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    controlPanel.setSize(800, 200);
    controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    controlPanel.add(musicControlPanel, BorderLayout.WEST);
    controlPanel.add(nowPlayingPanel, BorderLayout.CENTER);
    controlPanel.add(showQueue, BorderLayout.EAST);

    musicControlPanel.setBorder(new EmptyBorder(4, 16, 4, 16));
    nowPlayingPanel.setBorder(new EmptyBorder(4, 16, 4, 16));

    mainPanel.add(controlPanel, BorderLayout.SOUTH);
  }



  private void queueControl() {

    showQueue = new JButton(rightIcon);
    showQueue.setBorder(BorderFactory.createEmptyBorder());
    showQueue.setContentAreaFilled(false);

    // Create Queue Panel

    queuePanel = new JPanel(new BorderLayout());
    queuePanel.setBorder(BorderFactory.createLineBorder(Color.black));
    queuePanel.setVisible(false);

    queueTitle = new JLabel("Queue");
    queueTitle.setBorder(new EmptyBorder(4, 75, 0, 75));

    // Reloading Queue into the UI
    System.out.println("Current Queue: ");
    queuemodel = new DefaultListModel<String>();
    for (int i = 0; i < songQueue.size(); i++) {

      if (songQueue.get(i).getName().length() > 28) {
        queuemodel.add(i, songQueue.get(i).getName().substring(0, 25) + "...");
      } else {
        queuemodel.add(i, songQueue.get(i).getName());
      }
      System.out.println(songQueue.get(i).getName());
    }

    queuelist = new JList<String>(queuemodel);
    queuelist.setBorder(new EmptyBorder(4, 4, 4, 4));

    queuePanel.add(queueTitle, BorderLayout.NORTH);
    queuePanel.add(queuelist, BorderLayout.CENTER);

    if (viewQueue) {
      showQueue.setIcon(leftIcon);
      mainframe.setSize((mainframe.getWidth()), mainframe.getHeight());
      queuePanel.setVisible(true);
      queuePanel.repaint();
      queuePanel.validate();
      viewQueue = true;
    } else {
      showQueue.setIcon(rightIcon);
      mainframe.setSize((mainframe.getWidth() - 190), mainframe.getHeight());
      queuePanel.setVisible(false);
      queuePanel.repaint();
      queuePanel.validate();
      viewQueue = false;
    }

    showQueue.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        if (!viewQueue) {
          showQueue.setIcon(leftIcon);

          mainframe.setSize((mainframe.getWidth() + 191), mainframe.getHeight());
          queuePanel.setVisible(true);
          mainframe.repaint();
          mainframe.validate();
          viewQueue = true;
        } else {

          showQueue.setIcon(rightIcon);
          mainframe.setSize((mainframe.getWidth() - 191), mainframe.getHeight());
          queuePanel.setVisible(false);
          mainframe.repaint();
          mainframe.validate();
          viewQueue = false;
        }

      }
    });

    mainframe.add(queuePanel, BorderLayout.EAST);

  }

  /*
   * Create Playlist Panel
   */

  private void addPlaylistPanel() {
    playlistEntries = new ArrayList<>();
    playlistNames = getPlaylistDirectory();

    for (int i = 0; i < playlistNames.length; i++) {

      // Trims the string
      if (playlistNames[i].length() > 14) {
        playlistEntries.add(playlistNames[i].substring(0, 11) + "...");
      } else {
        playlistEntries.add(playlistNames[i]);
      }
    }

    JPanel playlistPanel = new JPanel(new BorderLayout());
    playlistPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel heading = new JLabel("Playlist");
    playlist = new JList(playlistEntries.toArray());
    playlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    playlist.setSelectedIndex(0);
    playlist.addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        int plistIndex = playlist.getSelectedIndex();
        selectedIndex = plistIndex;

        listmodel.clear();
        ArrayList<String> s = changeSonglist();
        for (int i = 0; i < s.size(); i++) {
          listmodel.addElement(s.get(i));
        }

        songlist.revalidate();
        songlist.repaint();
      }
    });
    playlistPanel.add(heading, BorderLayout.NORTH);
    playlistPanel.add(playlist, BorderLayout.CENTER);
    heading.setBorder(new EmptyBorder(4, 26, 0, 8));
    playlist.setBorder(new EmptyBorder(4, 4, 0, 4));

    mainPanel.add(playlistPanel, BorderLayout.WEST);
  }

  private void addSongListPanel() {
    JPanel songListPanel = new JPanel(new BorderLayout());
    songListPanel.setSize(400, 400);
    songListPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel heading = new JLabel("Song List");

    listmodel = new DefaultListModel<>();

    ArrayList<String> s = changeSonglist();
    for (int i = 0; i < s.size(); i++) {
      listmodel.addElement(s.get(i));
    }

    songlist = new JList<String>(listmodel);
    songlist.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          File selectedSong =
              new File(musicDirectory.getAbsolutePath() + "/"
                  + playlistNames[playlist.getSelectedIndex()] + "/" + songlist.getSelectedValue());
          songName = (String) songlist.getSelectedValue();

          if (player.isPlaying()) {
            player.stop();
          }
          player.setSong(selectedSong);
          player.play(-1);
        }
        if (e.getClickCount() == 1) {
          File selectedSong =
              new File(musicDirectory.getAbsolutePath() + "/"
                  + playlistNames[playlist.getSelectedIndex()] + "/" + songlist.getSelectedValue());

          songName = (String) songlist.getSelectedValue();
          if (!player.isPlaying())
            player.setSong(selectedSong);
        }
      }

      private Timer timer;

      @Override
      public void mousePressed(MouseEvent e) {
        timer = new Timer(500, new ActionListener() {
          public void actionPerformed(ActionEvent e) {

            File selectedSong =
                new File(musicDirectory.getAbsolutePath() + "/"
                    + playlistNames[playlist.getSelectedIndex()] + "/"
                    + songlist.getSelectedValue());

            songName = (String) songlist.getSelectedValue();

            int index = playlist.getSelectedIndex();
            songQueue.add(selectedSong);
            refreshQueue();
            playlist.setSelectedIndex(index);
          }
        });
        timer.start();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (timer != null) {
          timer.stop();
        }
      }

      @Override
      public void mouseEntered(MouseEvent e) {}

      @Override
      public void mouseExited(MouseEvent e) {}
    });

    heading.setBorder(new EmptyBorder(4, 8, 0, 16));
    songlist.setBorder(new EmptyBorder(4, 8, 0, 4));

    songListPanel.add(heading, BorderLayout.NORTH);
    songListPanel.add(songlist, BorderLayout.CENTER);

    mainPanel.add(songListPanel, BorderLayout.CENTER);
  }

  private void createMenuBar() {

    JMenuBar menubar = new JMenuBar();
    ImageIcon icon = new ImageIcon("exit.png");

    // FILE MENUBAR ITEM
    JMenu file = new JMenu("File");
    file.setMnemonic(KeyEvent.VK_F);

    JMenuItem selectDirectory = new JMenuItem("Change Directory", icon);
    selectDirectory.setMnemonic(KeyEvent.VK_E);
    selectDirectory.setToolTipText("Select Music Directory");
    selectDirectory.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        e.didLoad = false;
        refreshView();
      }
    });
    file.add(selectDirectory);
    JMenuItem exitSelection = new JMenuItem("Exit", icon);
    exitSelection.setMnemonic(KeyEvent.VK_E);
    exitSelection.setToolTipText("Exit application");
    exitSelection.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    });
    file.add(exitSelection);


    // SCHEDULE MENUBAR ITEM
    JMenu schedule = new JMenu("Schedule");
    schedule.setMnemonic(KeyEvent.VK_F);

    JMenuItem newSchedule = new JMenuItem("New Schedule", icon);
    newSchedule.setMnemonic(KeyEvent.VK_E);
    newSchedule.setToolTipText("Exit application");
    newSchedule.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    });
    schedule.add(newSchedule);

    // ALARM MENUBAR ITEM
    JMenu alarm = new JMenu("Alarm");
    alarm.setMnemonic(KeyEvent.VK_F);

    JMenuItem newAlarm = new JMenuItem("New Alarm", icon);
    newAlarm.setMnemonic(KeyEvent.VK_E);
    newAlarm.setToolTipText("Exit application");
    newAlarm.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    });
    alarm.add(newAlarm);

    menubar.add(file);
    menubar.add(schedule);
    menubar.add(alarm);

    mainframe.setJMenuBar(menubar);
  }


  private File[] getSongs() {
    if (playlistNames.length == 0) {
      File[] empty = new File[0];
      return empty;
    }

    File dir = new File(musicDirectory.getAbsolutePath() + "/" + playlistNames[selectedIndex]);
    File[] songList = dir.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".mp3");
      }
    });
    return songList;
  }

  private ArrayList<String> changeSonglist() {
    File[] songs = getSongs();

    ArrayList<String> songNames = new ArrayList<>();
    for (int i = 0; i < songs.length; i++) {
      songNames.add(songs[i].getName());
    }
    return songNames;
  }

  private String[] getPlaylistDirectory() {
    String[] directories = new String[0];

    if (!e.didLoad) {
      File tempDirectory = selectMusicDirectory();
      if (tempDirectory != null) {
        musicDirectory = tempDirectory;
        e.setPath(musicDirectory);
        saveProfile();
      }
    }
    try {
      directories = musicDirectory.list(new FilenameFilter() {
        @Override
        public boolean accept(File current, String name) {
          return new File(current, name).isDirectory();
        }
      });
    } catch (Exception ex) {
      e.didLoad = false;
      getPlaylistDirectory();
    }

    if (directories.length == 0) {
      String[] empty = new String[0];
      return empty;
    }
    return directories;
  }

  public File selectMusicDirectory() {
    // Set look and feel
    File selectedFile = null;
    JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.setDialogTitle("Select your music directory.");


    int returnVal = fc.showOpenDialog(fc);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      System.out.println("Opening: " + file.getName() + ". \n");
      selectedFile = file;
      // allows reuse of filechooser
      fc.setSelectedFile(new File(""));
    } else {

    }
    return selectedFile;
  }

  public saveData e;

  public void saveProfile() {
    try {
      FileOutputStream fileOut = new FileOutputStream("/tmp/saveData.ser");
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(e);
      out.close();
      fileOut.close();
      System.out.printf("Serialized data is saved in /tmp/saveData.ser");
    } catch (IOException i) {
      i.printStackTrace();
    }
  }

  public void loadProfile() {
    e = null;
    try {
      FileInputStream fileIn = new FileInputStream("/tmp/saveData.ser");
      ObjectInputStream in = new ObjectInputStream(fileIn);
      e = (saveData) in.readObject();
      in.close();
      fileIn.close();
    } catch (IOException i) {
      e = new saveData(false);
      return;
    } catch (ClassNotFoundException c) {
      e = new saveData(false);
      return;
    }

    e.didLoad = true;
    musicDirectory = e.musicPath;

  }

};
