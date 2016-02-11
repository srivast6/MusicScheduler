import java.util.*;

public class ScheduledPlay {
	
	// Main timer
	Timer t;
	
	// Class used to hold a scheduled song
	class scheduledSong extends TimerTask {
		
		String song;
		
		scheduledSong ( String song ) {
			this.song = song;
		}
		
        public void run() {
			// Play song
			System.out.println( "Playing song: " + song );
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
        }
    }
	
	// schedule song to be played at time
	ScheduledPlay ( Date time, String song ) {
		t = new Timer();
		t.schedule(new scheduledSong ( song ), time);
		System.out.println( "Song scheduled for: " + time );
	}
	
	// schedule list to be played at time
	ScheduledPlay ( Date time, Playlist list ) {
		t = new Timer();
		t.schedule(new scheduledPlaylist ( list ), time);
		System.out.println( "Playlist scheduled for: " + time );
	}
	
	// Cancel current timer
	public void cancel ( ) {
		t.cancel();
	}
	
	
	// main method for testing
	public static void main(String [] args)	{
		Playlist p1 = new Playlist ();
		String song = "test";
		Date now = new Date();
        
		p1.addSong("1");
		p1.addSong("2");
		p1.addSong("3");
		
		now.setSeconds(now.getSeconds() + 5);
		ScheduledPlay s1 =  new ScheduledPlay (now, song);
		now.setSeconds(now.getSeconds() + 5);
		ScheduledPlay s2 =  new ScheduledPlay (now, p1);
		
		System.out.println("Scheduleing done");
	}
	
}
