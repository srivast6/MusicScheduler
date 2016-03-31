// Lazy includes, clean if time allows
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
		return this.position + 1;
	}
		
	public int getSize ( ) {
		return this.playlist.size();
	}
	
	public void setPosition ( int p ) {
		this.position = p;
	}
	
	public String getName ( ) {
		return this.name;
	}
		
	public String getCurrentSong ( ) {
		return this.playlist.get(position);
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
	
	
	// Save playlist to a folder based on object
	public void save (MusicHome mh) {
		// directorty style may change for other OS
		File path = new File( mh.getMusicDirectory() + "/" + this.name);

		// if folder doesnt exists, then create it
		try {
			if (!path.exists()) {
				path.createNewFile();
			}

			for ( int i = 0; i < playlist.size(); i++ ) {
				Files.copy( Paths.get(playlist.get( i )), Paths.get(path.getAbsolutePath() + "/" + this.name ) );
			}

		} catch ( Exception e ) {
			System.out.println( "IO error " + e.getMessage() );
		} 
	}
	
	public void load ( String foldername ) {
		File path = new File( foldername );

		if (!path.exists()) {
			System.out.println( "Error, file does not exist: " + foldername );
			return;
		}
		System.out.println( "Loading playlist from: " + foldername );
		
		name = foldername.split("/")[ foldername.split("/").length -1 ];
		clear();
		
		File[] listOfFiles = path.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			System.out.println("File " + listOfFiles[i].getAbsolutePath());
			if ( listOfFiles[i].getName().endsWith(".mp3") ) {
				if ( i % 2 == 0 ) {
					addSong( listOfFiles[i].getAbsolutePath() );
				}
			}
		  }
		}
		Collections.reverse(playlist);
		setPosition( 0 );
    
	}
	
	// main method for testing
	public static void main(String [] args)	{
		Playlist p1 = new Playlist ( "test" );
		p1.addSong( "1" );
		p1.addSong( "2" );
		p1.addSong( "3" );

		p1.addSongNext("test");
		System.out.println( "Songs: " + p1.size() );
		
		p1.removeSong( 2 );
		System.out.println( "Songs: " + p1.size() );
		p1.setPosition( 0 );
		
		p1.clear();
		System.out.println( "Songs: " + p1.size() );
	}
	
} 
