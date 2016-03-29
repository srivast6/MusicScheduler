import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ScheduledPlay {
	
	// Main timer
	private Timer t;
	// path to song or playlist
	String file;
	private boolean isPlaylist;
	
	// Class used to hold a scheduled song
	// Not implemented directly in the gui
	// Left in for possible functionaliy of scheduling an indivual song without using a playlist
	class scheduledSong extends TimerTask {
		
		String song;
		private MusicHome musicHome;
		private MusicPlayer musicPlayer;
		private ArrayList<File> songQueue;
		
		scheduledSong ( String song, MusicHome mh) {
			this.song = song;
			this.musicHome = mh;
			this.musicPlayer = mh.getMusicPlayer();
			this.songQueue = mh.getSongQueue();
		}
		
        public void run() {
			// Play song
			System.out.println( "Playing song: " + song );				
			songQueue.add( new File ( song ) );
			musicHome.refreshQueue();
			musicPlayer.setSong(songQueue.remove(0));
			musicHome.refreshQueue();
			if ( !musicPlayer.isPlaying() ) {
				musicPlayer.play( -1 );
			}
			t.cancel();
        }
    }
	
	// Class used to hold a scheduled playlist
	class scheduledPlaylist extends TimerTask {
		
		Playlist list;
		private MusicHome musicHome;
		private MusicPlayer musicPlayer;
		private ArrayList<File> songQueue;
		
		scheduledPlaylist ( Playlist list, MusicHome mh) {
			this.list = list;
			this.musicHome = mh;
			this.musicPlayer = mh.getMusicPlayer();
			this.songQueue = mh.getSongQueue();
		}
		
        public void run() {
			list.load( musicHome.getMusicDirectory().getAbsolutePath() + "/" + list.getName() );
			// add all songs in Playlist to queue, refresh, and play
			for ( int i = 0; i < list.getSize(); i ++ ) {
				list.setPosition(i);
				songQueue.add( new File (list.getCurrentSong()) );
			} 
			musicHome.refreshQueue();
			musicPlayer.setSong(songQueue.remove(0));
            musicHome.refreshQueue();
			if ( !musicPlayer.isPlaying() ) {
				musicPlayer.play( -1 );
			}

			t.cancel();
        }
    }
	
	// schedule song to be played at time
	// Not implemented directly in gui becuase functionality is coverd by playlist
	ScheduledPlay ( Date time, String song, MusicHome mh ) {
		t = new Timer();
		isPlaylist = false;
		t.schedule( new scheduledSong ( song, mh ), time );
		System.out.println( "Song scheduled for: " + time );
	}
	
	// schedule list to be played at time
	ScheduledPlay ( Date time, Playlist list, MusicHome mh ) {
		t = new Timer();
		isPlaylist = true;
		t.schedule( new scheduledPlaylist ( list, mh ), time );
		System.out.println( "Playlist \"" + list.getName() + "\" scheduled for: " + time );
	}
	
	// Cancel current timer
	public void cancel ( ) {
		t.cancel();
	}
	
	
	// main method for testing
	// used before integrating with gui
	public static void main(String [] args)	{
		Playlist p1 = new Playlist ( "timer_test" );
		String song = "test";

		p1.addSong("1");
		p1.addSong("2");
		p1.addSong("3");

	}
	
}
