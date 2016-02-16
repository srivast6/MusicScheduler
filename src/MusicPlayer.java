import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer {
  public ArrayList<String> mp3Files = new ArrayList<String>();
  public File musicLocation;
  public File song;
  private Player player;
  private FileInputStream fis;
  private BufferedInputStream bis;
  private boolean paused;
  private int total;
  private int pauseLocation;


  public void setSong(File song) {
    this.song = song;
  }

  public void play(int pos) {
    try {

      fis = new FileInputStream(song.getAbsolutePath());
      total = fis.available();

      if (pos > -1)
        fis.skip(pos);

      bis = new BufferedInputStream(fis);
      player = new Player(bis);

    } catch (Exception e) {
      System.out.println("Problem playing file " + song.getName());
      System.out.println(e);
    }
    new Thread() {
      @Override
      public void run() {
        try {
          player.play();
        } catch (JavaLayerException e) {
          e.printStackTrace();
        }
      }
    }.start();

  }


  // File chooser for the music player, selects the directory to play music from.
  public void selectDirectory() {
    // Set look and feel
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.setDialogTitle("Select your music directory.");

    while (true) {
      int returnVal = fc.showOpenDialog(fc);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        System.out.println("Opening: " + file.getName() + ". \n");
        musicLocation = file;

        // allows reuse of filechooser
        fc.setSelectedFile(new File(""));
        break;
      } else {
        System.out.println("Select a file location");
      }
    }
  }

  public boolean isPaused() {
    return paused;
  }


  public void pause() {
    try {
      if (!paused) {
        // obtain location in the buffer
        pauseLocation = fis.available();
        player.close();

        // prepare to reuse objects
        fis = null;
        bis = null;
        player = null;

        // setting song as paused
        paused = true;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stop() {
    paused = false;
    player.close();
  }

  public void resume(File currentSong) {
    if (paused) {
      if (pauseLocation < total)
        play(total - pauseLocation);
      paused = false;
    }
  }


  // test client
  public static void main(String[] args) {

    Scanner in = new Scanner(System.in);
    MusicPlayer player = new MusicPlayer();
    player.selectDirectory();



    System.out.println("Directory name: " + player.musicLocation);

    // Lists songs available
    System.out.println("Available Songs: ");
    File[] songList = player.musicLocation.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".mp3");
      }
    });

    for (int i = 0; i < songList.length; i++) {
      System.out.println((i + 1) + ". " + songList[i].getName());
    }

    System.out.println("Type song number to play. (or 'help' for more features)");
    while (true) {
      System.out.print(">");
      String selection = in.nextLine();
      File currentSong;

      try {
        int songNum = Integer.parseInt(selection);

        if (songNum < 1 || songNum > songList.length) {
          System.out.println("Song number does not exist.");
        } else {
          currentSong = songList[songNum - 1];
          System.out.println("Playing: " + currentSong.getName());

          player.setSong(currentSong);
          player.play(-1);

          while (!player.player.isComplete() || player.isPaused()) {
            // Should not wait for response if song has ended
            String interruption = in.nextLine();

            switch (interruption.toUpperCase()) {
            // Pause playback of that song
              case "PAUSE":
                player.pause();
                break;
              // Resume playback
              case "RESUME":
                player.resume(currentSong);
                break;
              // Stop playback of that song completely
              case "STOP":
                player.stop();
                break;
            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        if (selection.equalsIgnoreCase("help")) {
          System.out.println("Exit: exits the application");
          System.out.println("Dir : changes the directory");
        }

        if (selection.equalsIgnoreCase("Exit")) {
          System.out.println("Exiting Application.");
          break;
        }
      }
    }
  }
}
