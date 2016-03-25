import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ScheduledPlay {
	
	// Main timer
	Timer t;
	Date scheduledTime;
	// path to song or playlist
	String file;
	private boolean isPlaylist;
	
	// Class used to hold a scheduled song
	class scheduledSong extends TimerTask {
		
		String song;
		
		scheduledSong ( String song ) {
			this.song = song;
		}
		
        public void run() {
			// Play song
			System.out.println( "Playing song: " + song );
			t.cancel();
        }
    }
	
	// Class used to hold a scheduled playlist
	class scheduledPlaylist extends TimerTask {
		
		Playlist list;
		
		scheduledPlaylist ( Playlist list ) {
			this.list = list;
		}
		
        public void run() {
			// Play all songs in Playlist
			while ( list.play() ) {
				System.out.println( "Playlist song # " + list.getPosition() );
			}
			t.cancel();
        }
    }
	
	// schedule song to be played at time
	ScheduledPlay ( Date time, String song ) {
		t = new Timer();
		file = song;
		isPlaylist = false;
		scheduledTime = time;
		t.schedule(new scheduledSong ( song ), time);
		System.out.println( "Song scheduled for: " + time );
	}
	
	// schedule list to be played at time
	ScheduledPlay ( Date time, Playlist list ) {
		t = new Timer();
		// only works for the current directory, probably need to update later
		file = list.getName() + ".playlist";
		isPlaylist = true;
		scheduledTime = time;
		t.schedule(new scheduledPlaylist ( list ), time);
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
	public static void main(String [] args)	{
		Playlist p1 = new Playlist ( "timer_test" );
		String song = "test";
		Date now = new Date();
        
		p1.addSong("1");
		p1.addSong("2");
		p1.addSong("3");
		
		now.setSeconds(now.getSeconds() + 5);
		ScheduledPlay s1 =  new ScheduledPlay (now, song);
		now.setSeconds(now.getSeconds() + 5);
		ScheduledPlay s2 =  new ScheduledPlay (now, p1);
		
		s1.save( "testTimer" );
		
		System.out.println("Scheduleing done");
	}
	
}
