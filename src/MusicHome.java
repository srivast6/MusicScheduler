import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.glass.events.KeyEvent;


/**
 * Created by gauravsrivastava on 2/6/16.
 */
public class MusicHome {

  private JFrame mainframe;
  private JPanel controlPanel;
  private JPanel musicControlPanel;
  private JPanel nowPlayingPanel;
  private JPanel mainPanel;
  private JButton play;
  private JButton pause;
  private JButton dir;
  private JLabel heading;
  private JLabel nowPlaying;
  private JList playlist;
  private JList songlist;

  private String songName;
  private MusicPlayer player;

  private File musicDirectory;

  ImageIcon pauseIcon;
  ImageIcon playIcon;
  ImageIcon stopIcon;

  // Required for event and change listeners
  private BooleanChangeListener listener;
  private BooleanEventListener isPlaying;

  ArrayList<String> playlistEntries;
  DefaultListModel<String> listmodel;
  String[] playlistNames;
  int selectedIndex = 0;

  public MusicHome() {
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

    } catch (Exception e) {
      e.printStackTrace();
    }

    mainframe = new JFrame("Beefed Up Music Player");
    mainframe.setSize(400, 400);
    mainframe.setLayout(new BorderLayout());
    addMainPanel();
    mainframe.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });


    createMenuBar();
    mainframe.setVisible(true);
  }

  private void addMainPanel() {
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.setSize(400, 400);
    addControlPanel();
    addPlaylistPanel();
    addSongListPanel();
    mainframe.add(mainPanel, BorderLayout.CENTER);

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

    dir = new JButton("Select Directory");
    dir.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        player.selectDirectory();
      }
    });

    musicControlPanel.add(play);
    musicControlPanel.add(pause);
    // musicControlPanel.add(dir);
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
    controlPanel = new JPanel(new BorderLayout());
    controlPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
    controlPanel.setSize(800, 200);
    controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    controlPanel.add(musicControlPanel, BorderLayout.WEST);
    controlPanel.add(nowPlayingPanel, BorderLayout.EAST);

    musicControlPanel.setBorder(new EmptyBorder(4, 16, 4, 16));
    nowPlayingPanel.setBorder(new EmptyBorder(4, 16, 4, 16));

    mainPanel.add(controlPanel, BorderLayout.SOUTH);
  }

  private void addPlaylistPanel() {
    playlistEntries = new ArrayList<>();

    playlistNames = getPlaylistDirectory();
    for (int i = 0; i < playlistNames.length; i++) {
      playlistEntries.add(playlistNames[i]);
    }

    JPanel playlistPanel = new JPanel(new BorderLayout());
    playlistPanel.setSize(600, 600);
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
        System.out.print(plistIndex);

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
    heading.setBorder(new EmptyBorder(4, 16, 0, 8));
    playlist.setBorder(new EmptyBorder(4, 16, 0, 16));

    mainPanel.add(playlistPanel, BorderLayout.WEST);
  }

  private void addSongListPanel() {
    JPanel songListPanel = new JPanel(new BorderLayout());
    songListPanel.setSize(600, 600);
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
              new File(musicDirectory.getAbsolutePath() + "/" + playlist.getSelectedValue() + "/"
                  + songlist.getSelectedValue());

          songName = (String) songlist.getSelectedValue();

          if (player.isPlaying()) {
            player.stop();
          }
          player.setSong(selectedSong);
          player.play(-1);
        }
        if (e.getClickCount() == 1) {
          File selectedSong =
              new File(musicDirectory.getAbsolutePath() + "/" + playlist.getSelectedValue() + "/"
                  + songlist.getSelectedValue());

          songName = (String) songlist.getSelectedValue();
          if (!player.isPlaying())
            player.setSong(selectedSong);
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

    heading.setBorder(new EmptyBorder(4, 16, 0, 16));
    songlist.setBorder(new EmptyBorder(4, 16, 0, 16));

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
        player.selectDirectory();
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
    musicDirectory = selectMusicDirectory();
    String[] directories = musicDirectory.list(new FilenameFilter() {
      @Override
      public boolean accept(File current, String name) {
        return new File(current, name).isDirectory();
      }
    });
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


};
