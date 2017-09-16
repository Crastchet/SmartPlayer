package me.crastchet.smartplayer.playlist;

import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by Thibault on 08/05/2017.
 */

public abstract class Playlist {

    protected List<File> trackList = new ArrayList<>();                   //list of all tracks
    private List<String> trackList_notPlayed = new ArrayList<>();       //list of tracks coming next (not played ones)
    //private HashSet<String> trackSet = new HashSet<>();               //set of all the tracks
    private int trackIndex = 0;                                         //position of the track we are on the trackList
    private int trackNumber = 0;                                        //number of tracks

    public void addtracks(Set tracks) {
        trackList.addAll(tracks);
        trackNumber += tracks.size(); //revoir pour opti peut etre
    }

    public void shuffleNotPlayedtracks() { //refaire avec File nan ?
        Random rd = new Random();
        int tmpIndex;
        String tmpTrack;
        for(int i=trackIndex; i<trackNumber; i++) {
            tmpIndex = rd.nextInt(trackNumber - 1 - trackIndex) + trackIndex + 1;
            tmpTrack = trackList_notPlayed.get(tmpIndex);
            trackList_notPlayed.remove(tmpTrack);
            trackList_notPlayed.add(tmpTrack);
        }
    }

    public abstract void orderTracks();


    public File[] getTracks() {
        return (File[]) trackList.toArray();
    }
}
