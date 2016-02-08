import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;

import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;



public class MusicPlayer {
  public ArrayList<String> mp3Files = new ArrayList<String>();
  public File musicLocation;
  private Player player;
  private AdvancedPlayer advPlayer;

  public MusicPlayer() {}

  public void play(File song) {
    try {
      FileInputStream fis = new FileInputStream(song.getAbsolutePath());
      BufferedInputStream bis = new BufferedInputStream(fis);
      player = new Player(bis);
      advPlayer = new AdvancedPlayer(bis);
    } catch (Exception e) { //
      System.out.println("Problem playing file " + song.getName());
      System.out.println(e);
    }
    new Thread() {
      @Override
      public void run() {
        try {
          player.play();
          advPlayer.setPlayBackListener(new PlaybackListener() {

          });
          while (!player.isComplete()) {
          }

        } catch (Exception e) {
          System.out.println(e);
        }
      }
    }.start();
  }

  public void selectDirectory() {

    JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.setDialogTitle("Select your music directory.");
    int returnVal = fc.showOpenDialog(fc);

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File file = fc.getSelectedFile();

      // This is where a real application would open the file.
      System.out.println("Opening: " + file.getName() + ". \n");
      musicLocation = file;
    } else {
      System.out.println("Open command cancelled by user. \n");
    }
  }

  // test client
  public static void main(String[] args) {
    boolean paused = false;

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
      Scanner in = new Scanner(System.in);
      String selection = in.nextLine();

      try {
        int songNum = Integer.parseInt(selection);

        if (songNum < 1 || songNum > songList.length) {
          System.out.println("Song number does not exist.");
        } else {
          System.out.println("Playing: " + songList[songNum - 1].getName());
          player.play(songList[songNum - 1]);

          while (!player.player.isComplete()) {
            String interuption = in.nextLine();
            if (interuption.equalsIgnoreCase("stop")) {
              player.player.close();
            }
          }
        }
      } catch (Exception e) {

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
