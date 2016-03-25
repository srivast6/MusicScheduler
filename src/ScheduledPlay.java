import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ScheduledPlay {
	
	// Main timer
	private Timer t;
	private Date scheduledTime;
	// path to song or playlist
	String file;
	private boolean isPlaylist;
	
	// Class used to hold a scheduled song
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
				//System.out.println( "Playlist song # " + list.getPosition() );
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
	ScheduledPlay ( Date time, String song, MusicHome mh ) {
		t = new Timer();
		file = song;
		isPlaylist = false;
		scheduledTime = time;
		t.schedule( new scheduledSong ( song, mh ), time );
		System.out.println( "Song scheduled for: " + time );
	}
	
	// schedule list to be played at time
	ScheduledPlay ( Date time, Playlist list, MusicHome mh ) {
		t = new Timer();
		isPlaylist = true;
		scheduledTime = time;
		t.schedule( new scheduledPlaylist ( list, mh ), time );
		System.out.println( "Playlist scheduled for: " + time );
	}
	
	// Cancel current timer
	public void cancel ( ) {
		t.cancel();
	}
	
	// Basic save function to write timer details to a text file
	/* FORMAT
	 * Song or Playlist
	 * filename of song or playlist
	 * date
	 */
	public void save ( String filename ) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		File file = new File( filename );
		// if file doesnt exists, then create it
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			if ( isPlaylist ) {
				bw.write( "Playlist\n" );
			} else {
				bw.write( "Song\n" );
			}
			
			bw.write( this.file + "\n" );
			bw.write( df.format(scheduledTime) );
			bw.close();
		} catch ( Exception e ) {
			System.out.println( "IO error " + e.getMessage() );
		} 
	}
	
	
	// main method for testing
	// used before integrating with gui
	public static void main(String [] args)	{
		Playlist p1 = new Playlist ( "timer_test" );
		String song = "test";
		Date now = new Date();
        
		p1.addSong("1");
		p1.addSong("2");
		p1.addSong("3");
		
		//now.setSeconds(now.getSeconds() + 5);
		//ScheduledPlay s1 =  new ScheduledPlay (now, song);
		//now.setSeconds(now.getSeconds() + 5);
		//ScheduledPlay s2 =  new ScheduledPlay (now, p1);
		
		// s1.save( "testTimer" );
		
		System.out.println("Scheduleing done");
	}
	
}
