package me.crastchet.smartplayer.playlist;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Thibault on 06/09/2017.
 */

public class LastAddedPlaylist extends Playlist {

    @Override
    public void orderTracks() {
        Collections.sort(trackList, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                if(file1.lastModified() < file2.lastModified()) return 1;
                else if(file1.lastModified() > file2.lastModified()) return -1;
                else return 0;
            }
        });
    }
}
