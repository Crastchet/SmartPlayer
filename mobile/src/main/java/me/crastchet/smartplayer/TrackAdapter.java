package me.crastchet.smartplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Thibault on 07/09/2017.
 */

public class TrackAdapter extends BaseAdapter {

    private ArrayList<Track> tracks;
    private LayoutInflater trackInf;

    public TrackAdapter(Context c, ArrayList<Track> theTracks) {
        tracks = theTracks;
        trackInf = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout trackLay = (LinearLayout) trackInf.inflate(R.layout.track_in_list_layout, parent, false);

        //get title and artist views
        TextView trackView = (TextView) trackLay.findViewById(R.id.track_title);
        TextView artistView = (TextView) trackLay.findViewById(R.id.track_artist);

        //get song using positions
        Track currentTrack = tracks.get(position);

        //get title and artist strings
        trackView.setText(currentTrack.getTitle());
        artistView.setText(currentTrack.getArtist());

        //set position as tag
        trackLay.setTag(position);
        return trackLay;
    }
}
