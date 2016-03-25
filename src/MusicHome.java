import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Created by gauravsrivastava on 2/6/16.
 */
public class MusicHome {

  // JFrames
  private JFrame mainframe;
  private JFrame alarmframe;

  // Panels
  private JPanel controlPanel;
  private JPanel musicControlPanel;
  private JPanel alarmControlPanel;
  private JPanel nowPlayingPanel;
  private JPanel mainPanel;
  private JPanel queuePanel;
  private JScrollPane songsScrollPane;
  private JScrollPane playlistScrollPane;
  private JScrollPane queueScrollPane;

  // Buttons
  private JButton play;
  private JButton pause;
  private JButton next;
  private JButton top;
  private JButton shuffle;
  private JButton delete;
  private JButton showQueue;
  private JButton btnAddAlarm;
  private JButton btnRemoveAlarm;

  // Labels
  private JLabel heading;
  private JLabel nowPlaying;
  private JLabel queueTitle;
  private JList playlist;
  private JList songlist;
  private JList queuelist;
  private boolean viewQueue;

  // Lists
  JList listAlarms;
  private ArrayList<File> songQueue;
  private ArrayList<Alarm> alarmList;
  private ArrayList<ScheduledAlarms> scheduledAlarmsList;
  private ArrayList<String> playlistEntries;
  private DefaultListModel<String> listmodel;
  private DefaultListModel<String> queuemodel;
  ArrayList<String> songNames;
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
  private ImageIcon nextIcon;
  private ImageIcon deleteIcon;
  private ImageIcon topIcon;
  private ImageIcon shuffleIcon;

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
    alarmList = new ArrayList<Alarm>();
    scheduledAlarmsList = new ArrayList<ScheduledAlarms>();

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
          nowPlaying.setText(player.getSongName());
          pause.setVisible(true);
          next.setVisible(true);
          play.setIcon(stopIcon);
        } else {

          if (!songQueue.isEmpty()) {
            player.setSong(songQueue.remove(0));
            refreshQueue();
            player.play(-1);
          }

          heading.setVisible(false);
          nowPlaying.setText("");
          pause.setVisible(false);
          pause.setIcon(pauseIcon);
          play.setIcon(playIcon);
          next.setVisible(false);
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
      nextIcon = new ImageIcon(ImageIO.read(new File("./images/next.png")));
      deleteIcon = new ImageIcon(ImageIO.read(new File("./images/delete.png")));
      topIcon = new ImageIcon(ImageIO.read(new File("./images/top.png")));
      shuffleIcon = new ImageIcon(ImageIO.read(new File("./images/shuffle.png")));

    } catch (Exception e) {
      e.printStackTrace();
    }

    mainframe = new JFrame("Beefed Up Music Player");
    mainframe.setSize(600, 400);
    mainframe.setLayout(new BorderLayout());
    mainframe.setMinimumSize(new Dimension(600, 400));

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
    controlPanel.remove(showQueue);
    queueControl();
    controlPanel.add(showQueue, BorderLayout.EAST);
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

        if (!songQueue.isEmpty() && !player.songSelected()) {
          player.setSong(songQueue.remove(0));
          refreshQueue();
        }

        if (player.songSelected()) {
          // Play the song
          if (!player.isPlaying()) {
            player.play(-1);
          }
          // Stop the song
          else {
            songQueue.clear();
            refreshQueue();
            player.stop();
            player.setSong(null);
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

    try {
      int initValue = (int) (Audio.getMasterOutputVolume() * 100);
      final JSlider volume = new JSlider(SwingConstants.HORIZONTAL);
      Dimension size = volume.getPreferredSize();
      size.height /= 1.8;
      size.width *= .80;
      volume.setPreferredSize(size);
      volume.setValue(initValue);


      volume.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {

          if (volume.getValue() == 0) {
            Audio.setMasterOutputMute(true);
            Audio.setMasterOutputVolume(0);
          } else {
            Audio.setMasterOutputMute(false);
            float normalizedVolume =
                (float) (((JSlider) e.getSource()).getValue() / (float) 100.00);
            Audio.setMasterOutputVolume(normalizedVolume);
          }
        }
      });

      volume.setBorder(new EmptyBorder(14, 0, 14, 0));

      musicControlPanel.add(volume);
    } catch (Exception volumeSliderException) {
      volumeSliderException.printStackTrace();
    }

  }

  private void currentSong() {
    nowPlayingPanel = new JPanel(new BorderLayout());
    nowPlayingPanel.setSize(400, 200);
    heading = new JLabel("Now Praying: ");
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

    JPanel containerPanel = new JPanel(new BorderLayout());

    Random gen = new Random();
    Color custom = new Color(gen.nextInt(255), gen.nextInt(255), gen.nextInt(255));

    containerPanel.setBorder(BorderFactory.createLineBorder(custom));

    queuePanel = new JPanel(new BorderLayout());
    queuePanel.setVisible(false);

    queueTitle = new JLabel("Queue");
    queueTitle.setBorder(new EmptyBorder(4, 75, 0, 75));

    queuemodel = new DefaultListModel<String>();
    for (int i = 0; i < songQueue.size(); i++) {
      queuemodel.add(i, (i + 1) + ".  " + songQueue.get(i).getName());
    }

    queuelist = new JList<String>(queuemodel);
    queuelist.setBorder(new EmptyBorder(4, 4, 4, 4));

    containerPanel.add(queueTitle, BorderLayout.NORTH);

    if (viewQueue) {
      showQueue.setIcon(leftIcon);
      mainframe.setSize((mainframe.getWidth()), mainframe.getHeight());
      queuePanel.setVisible(true);
      queuePanel.repaint();
      queuePanel.validate();
      viewQueue = true;
    } else {
      showQueue.setIcon(rightIcon);
      mainframe.setSize((mainframe.getWidth() - 220), mainframe.getHeight());
      queuePanel.setVisible(false);
      queuePanel.repaint();
      queuePanel.validate();
      viewQueue = false;
    }

    queueScrollPane = new JScrollPane(queuelist);
    queueScrollPane.setPreferredSize(new Dimension(220, mainframe.getHeight()));
    queueScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    queueScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    containerPanel.add(queueScrollPane, BorderLayout.CENTER);
    queuePanel.add(containerPanel, BorderLayout.CENTER);

    JPanel queueControls = new JPanel();

    next = new JButton(nextIcon);
    next.setBorder(BorderFactory.createEmptyBorder());
    next.setContentAreaFilled(false);
    next.setBorder(new EmptyBorder(4, 10, 4, 4));
    if (player == null || !player.isPlaying()) {
      next.setVisible(false);
    }

    next.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        player.stop();
      }
    });

    top = new JButton(topIcon);
    top.setBorder(BorderFactory.createEmptyBorder());
    top.setContentAreaFilled(false);
    top.setBorder(new EmptyBorder(4, 10, 4, 10));

    top.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        songQueue.add(0, songQueue.remove(queuelist.getSelectedIndex()));
        refreshQueue();
      }
    });

    delete = new JButton(deleteIcon);
    delete.setBorder(BorderFactory.createEmptyBorder());
    delete.setContentAreaFilled(false);
    delete.setBorder(new EmptyBorder(4, 4, 4, 10));

    delete.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!songQueue.isEmpty()) {
          songQueue.remove(queuelist.getSelectedIndex());
          refreshQueue();
        }
      }
    });

    shuffle = new JButton(shuffleIcon);
    shuffle.setBorder(BorderFactory.createEmptyBorder());
    shuffle.setContentAreaFilled(false);
    shuffle.setBorder(new EmptyBorder(4, 10, 4, 10));

    shuffle.addActionListener(new ActionListener() {
      @SuppressWarnings("unchecked")
      @Override
      public void actionPerformed(ActionEvent e) {
        Random gen = new Random();
        Random seed = new Random();
        gen.setSeed(seed.nextLong());
        ArrayList<File> tempQueue = new ArrayList<File>();
        tempQueue = (ArrayList<File>) songQueue.clone();
        songQueue.clear();

        while (!tempQueue.isEmpty()) {
          songQueue.add(tempQueue.remove(gen.nextInt(tempQueue.size())));
        }
        refreshQueue();
      }
    });


    queueControls.add(delete);
    queueControls.add(top);
    queueControls.add(shuffle);
    queueControls.add(next);
    queueControls.setBorder(BorderFactory.createLineBorder(Color.black));

    queuePanel.add(queueControls, BorderLayout.SOUTH);

    showQueue.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        if (!viewQueue) {
          showQueue.setIcon(leftIcon);
          mainframe.setSize((mainframe.getWidth() + 220), mainframe.getHeight());
          queuePanel.setVisible(true);
          mainframe.repaint();
          mainframe.validate();
          viewQueue = true;
        } else {

          showQueue.setIcon(rightIcon);
          mainframe.setSize((mainframe.getWidth() - 220), mainframe.getHeight());
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
      playlistEntries.add(playlistNames[i]);
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

        songNames.clear();
        listmodel.clear();
        songNames = changeSonglist();
        for (int i = 0; i < songNames.size(); i++) {
          listmodel.addElement((i + 1) + ".  " + songNames.get(i));
        }

        songlist.revalidate();
        songlist.repaint();
      }
    });

    playlist.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          for (int i = 0; i < songNames.size(); i++) {
            File selectedSong =
                new File(musicDirectory.getAbsolutePath() + "/"
                    + playlistNames[playlist.getSelectedIndex()] + "/" + songNames.get(i));
            songQueue.add(selectedSong);
            if (!viewQueue) {
              showQueue.doClick();
            }
            refreshQueue();
          }
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {}

      @Override
      public void mouseReleased(MouseEvent e) {}

      @Override
      public void mouseEntered(MouseEvent e) {}

      @Override
      public void mouseExited(MouseEvent e) {}

    });

    playlistPanel.add(heading, BorderLayout.NORTH);

    playlistScrollPane = new JScrollPane(playlist);
    playlistScrollPane.setPreferredSize(new Dimension(120, 300));
    playlistScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    playlistScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    playlistPanel.add(playlistScrollPane, BorderLayout.CENTER);

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

    songNames = changeSonglist();
    for (int i = 0; i < songNames.size(); i++) {
      if (i % 2 == 0) {
        listmodel.addElement(songNames.get(i));
      } else {
        listmodel.addElement((i + 1) + ".  " + songNames.get(i));
      }
    }

    songlist = new JList<String>(listmodel);
    songlist.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          File selectedSong =
              new File(musicDirectory.getAbsolutePath() + "/"
                  + playlistNames[playlist.getSelectedIndex()] + "/"
                  + songNames.get(songlist.getSelectedIndex()));
          songName = (String) songlist.getSelectedValue();

          if (songlist.getSelectedIndex() == 6)
            System.exit(0);

          if (player.isPlaying()) {
            player.stop();
          }
          player.setSong(selectedSong);
          player.play(-1);
        }
        if (e.getClickCount() == 1) {
          File selectedSong =
              new File(musicDirectory.getAbsolutePath() + "/"
                  + playlistNames[playlist.getSelectedIndex()] + "/"
                  + songNames.get(songlist.getSelectedIndex()));

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
                    + songNames.get(songlist.getSelectedIndex()));

            songName = (String) songlist.getSelectedValue();

            int index = playlist.getSelectedIndex();
            songQueue.add(selectedSong);
            refreshQueue();
            if (!viewQueue) {
              showQueue.doClick();
            }
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

    songsScrollPane = new JScrollPane(songlist);
    songsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    songsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    songListPanel.add(songsScrollPane, BorderLayout.CENTER);

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
        // System.exit(0);
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
        PlaylistScheduleGUI p = new PlaylistScheduleGUI(playlistEntries);
        p.prepareScheduleGUI();
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
        alarmSetGui();
        // System.exit(0);
      }
    });
    alarm.add(newAlarm);

    // ALARM MENUBAR ITEM
    JMenu aboutMenu = new JMenu("About");
    aboutMenu.setMnemonic(KeyEvent.VK_F);

    aboutMenu.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        JFrame about = new JFrame("About");
        about.setSize(280, 170);
        about.setLayout(new BorderLayout());
        about.setResizable(false);

        about.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent windowEvent) {
            about.removeAll();
          }
        });

        JPanel content = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Developers");
        title.setBorder(new EmptyBorder(4, 110, 4, 0));
        JTextArea developers =
            new JTextArea("William Vanschaik (wvanscha@purdue.edu) \n"
                + "Joey Imburgia (jimburgi@purdue.edu) \n"
                + "Santiago Abondano (sabonda@purdue.edu) \n"
                + "Rachel Gully (rgully@purdue.edu) \n"
                + "Gaurav Srivastava (srivast6@purdue.edu) \n");
        // developers.setEditable(false);
        developers.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel icons = new JLabel("Icons by Flaticons.com");
        icons.setBorder(new EmptyBorder(4, 70, 4, 0));

        content.add(title, BorderLayout.NORTH);
        content.add(developers, BorderLayout.CENTER);
        content.add(icons, BorderLayout.SOUTH);

        about.add(content, BorderLayout.CENTER);

        about.setVisible(true);

      }

      @Override
      public void mousePressed(MouseEvent e) {}

      @Override
      public void mouseReleased(MouseEvent e) {}

      @Override
      public void mouseEntered(MouseEvent e) {}

      @Override
      public void mouseExited(MouseEvent e) {}
    });

    alarm.add(newAlarm);

    menubar.add(file);
    menubar.add(schedule);
    menubar.add(alarm);
    menubar.add(aboutMenu);

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

  public String[] getPlaylistDirectory() {
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

  public File selectAlam() {
    // Set look and feel
    File selectedFile = null;
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(musicDirectory);
    fc.setDialogTitle("Select your alarm");


    int returnVal = fc.showOpenDialog(fc);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();
      selectedFile = file;
      fc.setSelectedFile(new File(""));
    } else {

    }
    return selectedFile;
  }

  public File selectMusicDirectoryFile() {

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select your alarm sound!");
    FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 File", "mp3");
    fileChooser.setFileFilter(filter);
    fileChooser.showOpenDialog(fileChooser);
    File file = fileChooser.getSelectedFile();
    return file;
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

  public void deleteAlarmsPassed() {
    Calendar nowDate = Calendar.getInstance();
    for (int i = 0; i < alarmList.size(); i++) {
      if (alarmList.get(i).getAlarmTimeCalendarObjectClean().before(nowDate)) {

        for (int b = 0; b < scheduledAlarmsList.size(); b++) {
          if (alarmList.get(i).getAlarmTimeCalendarObjectClean() == scheduledAlarmsList.get(b).alarmScheduled
              .getAlarmTimeCalendarObjectClean()) {
            scheduledAlarmsList.get(b).t.cancel();
            scheduledAlarmsList.remove(b);
          }
        }

        alarmList.remove(i);
      }

    }
  }


  public void alarmSetGui() {
    if (alarmframe != null) {
      if (alarmframe.isVisible()) {
        alarmframe.dispatchEvent(new WindowEvent(alarmframe, WindowEvent.WINDOW_CLOSING));
      }
    }
    MusicHome passingView = this;
    ArrayList<String> alarmsInList = new ArrayList<String>();
    for (int i = 0; i < alarmList.size(); i++) {
      alarmsInList.add(alarmList.get(i).getAlarmTime());
    }
    Calendar nowDate = Calendar.getInstance();
    for (int i = 0; i < alarmList.size(); i++) {
      if (alarmList.get(i).getAlarmTimeCalendarObjectClean().before(nowDate)) {

        for (int b = 0; b < scheduledAlarmsList.size(); b++) {
          if (alarmList.get(i).getAlarmTimeCalendarObjectClean() == scheduledAlarmsList.get(b).alarmScheduled
              .getAlarmTimeCalendarObjectClean()) {
            scheduledAlarmsList.get(b).t.cancel();
            scheduledAlarmsList.remove(b);
          }
        }

        alarmList.remove(i);
      }

    }

    alarmframe = new JFrame("Alarms");
    alarmframe.setBounds(100, 100, 220, 300);
    JPanel mainPanel = new JPanel(new BorderLayout());

    Integer[] zeroThroughTwelve = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    Integer[] zeroThroughFiftyNine =
        {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
            25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46,
            47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59};
    String[] amOrPm = {"AM", "PM"};

    JPanel alarmCreation = new JPanel(new BorderLayout());

    JPanel comboboxPanel = new JPanel(new BorderLayout());

    // HOUR
    JPanel hourPanel = new JPanel(new BorderLayout());
    JComboBox<Integer> comboBox = new JComboBox<Integer>(zeroThroughTwelve);

    JLabel lblHour = new JLabel("Hr");
    lblHour.setBorder(new EmptyBorder(4, 20, 0, 4));

    hourPanel.add(comboBox, BorderLayout.CENTER);
    hourPanel.add(lblHour, BorderLayout.NORTH);

    comboboxPanel.add(hourPanel, BorderLayout.WEST);

    // MINUTE
    JPanel minutePanel = new JPanel(new BorderLayout());
    JComboBox<Integer> comboBox_1 = new JComboBox<Integer>(zeroThroughFiftyNine);

    JLabel lblMin = new JLabel("Min");
    lblMin.setBorder(new EmptyBorder(4, 20, 0, 4));

    minutePanel.add(comboBox_1, BorderLayout.CENTER);
    minutePanel.add(lblMin, BorderLayout.NORTH);

    comboboxPanel.add(minutePanel, BorderLayout.CENTER);

    // AM
    JPanel amPanel = new JPanel(new BorderLayout());
    JComboBox<String> comboBox_2 = new JComboBox<String>(amOrPm);
    JLabel lblAmpm = new JLabel("Am/Pm");
    lblAmpm.setBorder(new EmptyBorder(4, 15, 0, 4));

    amPanel.add(comboBox_2, BorderLayout.CENTER);
    amPanel.add(lblAmpm, BorderLayout.NORTH);


    comboboxPanel.add(amPanel, BorderLayout.EAST);

    alarmCreation.add(comboboxPanel, BorderLayout.CENTER);

    listAlarms = new JList(alarmsInList.toArray()); // data has type Object[]
    listAlarms.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    listAlarms.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    listAlarms.setVisibleRowCount(-1);
    JScrollPane listScroller = new JScrollPane(listAlarms);
    listScroller.setPreferredSize(new Dimension(250, 80));

    JPanel alarmListPanel = new JPanel(new BorderLayout());
    listAlarms.setBorder(BorderFactory.createLineBorder(Color.black));
    alarmListPanel.add(listAlarms, BorderLayout.CENTER);

    btnAddAlarm = new JButton("Add Alarm");
    btnAddAlarm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        Alarm newAlarm = new Alarm();

        int hour;
        if (comboBox_2.getSelectedItem() == "PM") {
          if ((int) comboBox.getSelectedItem() != 12)
            hour = (int) comboBox.getSelectedItem() + 12;
          else
            hour = 12;

        } else {
          if ((int) comboBox.getSelectedItem() != 12)
            hour = (int) comboBox.getSelectedItem();
          else
            hour = 0;
        }


        Calendar currentTime = Calendar.getInstance();
        newAlarm.setAlarmTimeWithInts(hour, comboBox_1.getSelectedIndex());
        // If User tries to do for a time that has already passed make it for the next day
        if (newAlarm.getAlarmTimeCalendarObject().before(currentTime))
          newAlarm.addDay(1);

        File alarmSound = selectAlam();

        if (alarmSound == null)
          return;

        newAlarm.setAlarmSound(alarmSound);
        newAlarm.setSnoozeLengthInMinutes(PromptBox.snoozeLengthPicker());
        alarmList.add(newAlarm);
        ScheduledAlarms schedAlarm = new ScheduledAlarms(newAlarm, player, passingView);
        scheduledAlarmsList.add(schedAlarm);
        alarmframe.dispatchEvent(new WindowEvent(alarmframe, WindowEvent.WINDOW_CLOSING));
        alarmSetGui();
      }
    });
    alarmCreation.add(btnAddAlarm, BorderLayout.SOUTH);

    btnRemoveAlarm = new JButton("Remove Alarm");
    btnRemoveAlarm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (!listAlarms.isSelectionEmpty()) {

          // cancels timer
          for (int i = 0; i < scheduledAlarmsList.size(); i++) {
            if (alarmList.get(listAlarms.getSelectedIndex()).getAlarmTimeCalendarObjectClean() == scheduledAlarmsList
                .get(i).alarmScheduled.getAlarmTimeCalendarObjectClean()) {
              scheduledAlarmsList.get(i).t.cancel();
              scheduledAlarmsList.remove(i);
            }
          }

          alarmList.remove(listAlarms.getSelectedIndex());

          alarmframe.dispatchEvent(new WindowEvent(alarmframe, WindowEvent.WINDOW_CLOSING));
          alarmSetGui();
        }
      }
    });

    alarmListPanel.add(btnRemoveAlarm, BorderLayout.SOUTH);
    JLabel setAlarms = new JLabel("Set Alarms");
    setAlarms.setBorder(new EmptyBorder(4, 80, 0, 4));
    alarmListPanel.add(setAlarms, BorderLayout.NORTH);

    mainPanel.add(alarmListPanel, BorderLayout.CENTER);
    mainPanel.add(alarmCreation, BorderLayout.NORTH);

    alarmframe.add(mainPanel);
    alarmframe.setResizable(false);
    alarmframe.setVisible(true);
  }

};
