package me.crastchet.smartplayer;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Thibault on 14/09/2017.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    //media player
    private MediaPlayer player;
    //track list
    private ArrayList<Track> tracks;
    //current position
    private int trackPosition;

    private final IBinder musicBond = new MusicBinder();

    public void onCreate() {
        //create the service
        super.onCreate();
        //initialize position
        trackPosition = 0;
        //create player
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBond;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
    }

    public void setList(ArrayList<Track> theTracks) {
        this.tracks = theTracks;
    }

    public class MusicBinder extends Binder {
        MusicService getService() { return MusicService.this; }
    }

    public void playTrack() {
        player.reset();

        //get track
        Track playTrack = tracks.get(trackPosition);
        //get id
        long currentTrack = playTrack.getId();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentTrack);

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (IOException e) {
            Log.e("MUSIC SERVICE", "Error seting data source", e);
        }

        player.prepareAsync();
    }

    public void setTrack(int trackIndex) {
        trackPosition = trackIndex;
    }
}
