package com.cs408.musicscheduler;

import javafx.scene.control.SelectionMode;
import org.omg.CORBA.PRIVATE_MEMBER;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by gauravsrivastava on 2/6/16.
 */
public class MusicHome {

    private JFrame mainframe;
    private JPanel controlPanel;
    private JPanel musicControlPanel;
    private JPanel nowPlayingPanel;
    private JPanel mainPanel;
    private GridBagConstraints c;
    private JButton play;
    private JButton pause;
    private JLabel nowPlaying;
    private JList playlist;
    private JTable songlist;
    ArrayList<String> playlistEntries;
    SongTableModel t;

    public MusicHome() {
        prepareGUI();
    }

    public static void main(String[] args) {
        MusicHome musicHome = new MusicHome();
    }

    private void prepareGUI() {
        mainframe = new JFrame("Beefed Up Music Player");
        mainframe.setSize(400, 400);
        mainframe.setLayout(new GridLayout(1,1));
        addMainPanel();
        mainframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        mainframe.setVisible(true);
    }
    private void addMainPanel() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setSize(400, 400);
        c = new GridBagConstraints();
        addControlPanel();
        addPlaylistPanel();
        addSongListPanel();
        mainframe.add(mainPanel);
    }
    private void musicControl() {
        musicControlPanel = new JPanel();
        musicControlPanel.setSize(400, 200);
        play = new JButton("Play");
        pause = new JButton("Pause");
        musicControlPanel.add(play);
        musicControlPanel.add(pause);
    }

    private void currentSong() {
        nowPlayingPanel = new JPanel(new BorderLayout());
        nowPlayingPanel.setSize(400, 200);
        JLabel heading = new JLabel("Now Playing");
        nowPlaying = new JLabel("Hello-Adele");
        nowPlayingPanel.add(heading, BorderLayout.NORTH);
        nowPlayingPanel.add(nowPlaying, BorderLayout.CENTER);
    }

    private void addControlPanel() {
        musicControl();
        currentSong();
        controlPanel = new JPanel();
        controlPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        controlPanel.setSize(800, 200);
        controlPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        controlPanel.add(musicControlPanel);
        controlPanel.add(nowPlayingPanel);
        c.anchor = GridBagConstraints.PAGE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 2;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 0;
        mainPanel.add(controlPanel, c);
    }

    private void addPlaylistPanel() {
        playlistEntries = new ArrayList<>();
        playlistEntries.add("Happy");
        playlistEntries.add("Pump Up");
        playlistEntries.add("Mellow");
        JPanel playlistPanel = new JPanel();
        playlistPanel.setSize(600, 600);
        playlistPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        playlistPanel.setLayout(new BoxLayout(playlistPanel, BoxLayout.PAGE_AXIS));
        JLabel heading = new JLabel("Playlist");
        playlist = new JList(playlistEntries.toArray());
        playlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playlist.setSelectedIndex(0);
        playlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int plistIndex = playlist.getSelectedIndex();
                t.selectedIndex = plistIndex;
                System.out.print(plistIndex);
                songlist.revalidate();
                songlist.repaint();
            }
        });
        playlistPanel.add(heading);
        playlistPanel.add(playlist);
        c.anchor=GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        mainPanel.add(playlistPanel, c);
    }

    private void addSongListPanel() {
        JPanel songListPanel = new JPanel();
        songListPanel.setSize(600, 600);
        songListPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        songListPanel.setLayout(new BoxLayout(songListPanel, BoxLayout.PAGE_AXIS));
        JLabel heading = new JLabel("Song List");
        t = new SongTableModel();
        songlist = new JTable(t);
        songlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songlist.setRowSelectionAllowed(true);
        songlist.setFillsViewportHeight(true);
        songlist.setShowGrid(true);
        songListPanel.add(heading);
        songListPanel.add(songlist.getTableHeader());
        songListPanel.add(songlist);
        c.anchor=GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3;
        c.weightx = 1;
        mainPanel.add(songListPanel, c);
    }

};
