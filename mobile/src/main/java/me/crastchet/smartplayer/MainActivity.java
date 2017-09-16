package me.crastchet.smartplayer;

import me.crastchet.smartplayer.MusicService.MusicBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

//    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
//    HashSet<File> trackSetFile = new HashSet<File>();
//    ArrayList<String> trackListName = new ArrayList<>();

    private ArrayList<Track> trackList;
    private ListView trackView;

    private MusicService musicService;
    private Intent playIntent;
    private boolean musicBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trackView = (ListView) findViewById(R.id.trackList);
        trackList = new ArrayList<Track>();
        getTrackList();

        TrackAdapter trackAdapter = new TrackAdapter(this, trackList);
        trackView.setAdapter(trackAdapter);

        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder) service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(trackList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }


    /**
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fillSetFromFolder("Posey");
                    renderPlaylist(getPlaylist());
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void fillSetFromFolder(String folder) {
        File directory = new File(Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_MUSIC + "/" + folder);
        fillSoundSet(directory);
    }

    private Playlist getPlaylist() { //on sait que ça va pas être comme ça plus tard
        Playlist playlist = new LastAddedPlaylist();
        playlist.addtracks(trackSetFile);
        playlist.orderTracks();
        return playlist;
    }

    private void renderPlaylist(Playlist playlist) {
        trackListName.clear();
        for(File f : playlist.getTracks())
            trackListName.add(f.getName());


        ArrayAdapter<String> la = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trackListName);
        //lv.setAdapter(la);

        if(!trackListName.isEmpty())
            ( (TextView) findViewById(R.id.empty) ).setText("");
    }

    /**
     * Get all the songs from a given directory, and them to the set of songs
     * param directory Thi directory from the one we want the songs
     **

    private void fillSoundSet(File directory) {
        if(directory.isDirectory())
            for(File f : directory.listFiles())
                fillSoundSet(f);
        else
            trackSetFile.add(directory);
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_shuffle:
                //shuffle
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicService = null;
                System.exit(0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    public void getTrackList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                trackList.add(new Track(thisId, thisTitle, thisArtist));
            } while (musicCursor.moveToNext());
        }
    }

    public void trackPicked(View view) {
        musicService.setTrack(Integer.parseInt(view.getTag().toString()));
        musicService.playTrack();
    }




}
