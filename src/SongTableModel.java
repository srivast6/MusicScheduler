package com.cs408.musicscheduler;

import javax.swing.table.AbstractTableModel;

/**
 * Created by gauravsrivastava on 2/11/16.
 */
public class SongTableModel extends AbstractTableModel{
    public int selectedIndex = 0;
    public String[] columnNames = {"Song",
            "Artist",
            "Album",
            "Playlist"};
    public Object[][] happySong = {
            {"Jingle Bells", "Smith", "Christmas", "Happy"},
            {"Sweet Caroline", "Smith", "Harrys", "Happy"},
            {"Do you Realize", "Flaming Lips", "Lips", "Happy"}
    };
    public Object[][] pumpupSongs = {
            {"Watch out for this", "Major Lazor", "Christmas", "Happy"},
            {"Sweet Caroline", "Smith", "Harrys", "Happy"},
    };
    public Object[][] mellowSongs = {
            {"Singing in the Rain", "Gene Kelly", "volume 1", "Mellow"},
    };

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public int getRowCount() {
        if(selectedIndex == 0) {
            return happySong.length;
        }
        else if(selectedIndex == 1) {
            return pumpupSongs.length;
        }
        else {
            return mellowSongs.length;
        }

    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(selectedIndex == 0) {
            return happySong[rowIndex][columnIndex];
        }
        else if(selectedIndex == 1) {
            return pumpupSongs[rowIndex][columnIndex];
        }
        else {
            return mellowSongs[rowIndex][columnIndex];
        }
    }

    public void update(){

    }
}
