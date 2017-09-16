package me.crastchet.smartplayer;

/**
 * Created by Thibault on 07/09/2017.
 */

public class Track {
//a effacer (pour github)
    private long id;
    private String title;
    private String artist;



    public Track(long trackID, String trackTitle, String trackArtist) {
        id = trackID;
        title = trackTitle;
        artist = trackArtist;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }
}
