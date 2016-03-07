import java.util.*;
import java.io.*;
import java.nio.file.*;


public class Playlist {
	
	// List to store song names as String
	private ArrayList<String> playlist;
	// Current postion in the playlist
	private int position;
	// Current postion in the playlist
	private String name;
	
	
	// Default contructor
	public Playlist ( String name ) {
		this.playlist = new ArrayList<String>();
		this.position = 0;
		this.name = name;
	}
	
	// Helper method to make sure playlist is intialized
	public boolean checkPlaylist ( ArrayList<String> p ) {
		if ( p != null ) {
			return true;
		} else {
			System.out.println( "Error: Playlist not initialized!" );
		}
		return false;
	}
	
	public int size ( ) {
		if ( checkPlaylist( playlist ) ) {
			return playlist.size( );
		} 
		return 0;
	}
	
	public boolean isEmpty( ) {
		if ( checkPlaylist( playlist ) ) {
			if ( playlist.size( ) > 0 ) {
				return false;
			}
		} 
		return true;
	}
	
	public int getPosition ( ) {
		return this.position;
	}
	
	public void setPosition ( int p ) {
		this.position = p;
	}
	
	public String getName ( ) {
		return this.name;
	}
	
	public void setName ( String n ) {
		this.name = n;
	}	
	
	// add a String song to the end of the playlist
	public void addSong ( String song ) {
		if ( checkPlaylist( playlist ) ) {
			playlist.add( song );
		} 
	}
	
	// add a song to playlist after current position
	public void addSongNext ( String song ) {
		if ( checkPlaylist( playlist ) ) {
			playlist.add( position + 1 , song );
		}
	}
	
	// remove song with String name song
	public void removeSong ( String song ) {
		if ( checkPlaylist( playlist ) ) {
			playlist.remove( song );
		} 
	}
	
	// remove song from playlist at index
	public void removeSong ( int index ) {
		if ( checkPlaylist( playlist ) ) {
			playlist.remove( index );
		} 
	}
	
	// remove all songs from playlist
	public void clear ( ) {
		if ( checkPlaylist( playlist ) ) {
			playlist.clear( );
			position = 0;
		} 
	}
	
	// plays song from playlist at current position returns true if there is another song
	// Use in a while loop to play an entire playlist
	public boolean play ( ) {
		if ( !checkPlaylist( playlist ) ) {
			return false;
		} 
		if ( position >= playlist.size() ) {
			System.out.println( "Error: End of playlist. can not play" );
			return false;
		}
		String song = playlist.get( position );
		
		System.out.println( "Now Playing: " + song );
		//play( song );
		
		position++;
		if ( position >= playlist.size() ) {
			return false;
		}
		
		return true;
	}
	
	// Save playlist to a folder based on object
	public void save () {
		// directorty style may change for other OS
		File path = new File(System.getProperty("user.dir") + "/" + this.name);

		// if folder doesnt exists, then create it
		try {
			if (!path.exists()) {
				path.createNewFile();
			}

			for ( int i = 0; i < playlist.size(); i++ ) {
				Files.copy( Paths.get(playlist.get( i )), Paths.get(path.getAbsolutePath() + "/" + playlist.get( i ).split("/|\\")[ playlist.get( i ).split("/|\\").length -1 ]) );
			}

		} catch ( Exception e ) {
			System.out.println( "IO error " + e.getMessage() );
		} 
	}
	
	// load playlist from a folder and set up object
	public void load ( String foldername ) {
		File path = new File( foldername );

		if (!path.exists()) {
			System.out.println( "Error, file does not exist: " + foldername );
			return;
		}
		
		name = foldername.split("/|\\")[ foldername.split("/|\\").length -1 ];
		clear();
		
		File[] listOfFiles = path.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			System.out.println("File " + listOfFiles[i].getName());
			if ( listOfFiles[i].getName().endsWith(".mp3") ) {
				addSong( listOfFiles[i].getName() );
			}
		  }
		}
    
	}
	
	
	// main method for testing
	public static void main(String [] args)	{
		Playlist p1 = new Playlist ( "test" );
		p1.addSong( "1" );
		p1.addSong( "2" );
		p1.addSong( "3" );
		p1.play();
		p1.addSongNext("test");
		System.out.println( "Songs: " + p1.size() );
		while ( p1.play() ) {
			System.out.println();
		}
		
		p1.removeSong( 2 );
		System.out.println( "Songs: " + p1.size() );
		p1.setPosition( 0 );
		while ( p1.play() ) {
			System.out.println();
		}
		
		p1.save();
		
		p1.clear();
		System.out.println( "Songs: " + p1.size() );
	}
	
} 
