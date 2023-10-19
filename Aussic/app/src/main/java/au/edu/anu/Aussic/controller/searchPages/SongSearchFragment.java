package au.edu.anu.Aussic.controller.searchPages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.Adapter.GeneralItem;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListSongAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnGeneralItemClickListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataArrivedListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataChangeListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.entity.Song;

/**
 * @author: u7516507, Evan Cheung
 */

/**
 * A simple {@link Fragment} subclass.
 * The filter of Song results displaying
 */
public class SongSearchFragment extends Fragment implements OnDataArrivedListener, OnDataChangeListener, OnGeneralItemClickListener {

    private TextView songs;
    private RecyclerView searchSongRecyclerView;

    public SongSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RuntimeObserver.addOnDataArrivedListener(this);
        RuntimeObserver.addOnDataChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.songs = view.findViewById(R.id.search_songs_songs);
        this.searchSongRecyclerView = view.findViewById(R.id.search_list_song_song_recyclerView);

        onDataArrivedResponse();
    }

    @Override
    public void onDataArrivedResponse() {
        if(RuntimeObserver.getCurrentSongList() != null && !RuntimeObserver.getCurrentSongList().isEmpty()) {
            List<GeneralItem> songList = new ArrayList<>();

            for (Song song : RuntimeObserver.currentSearchResultSongs)  songList.add(new GeneralItem(song));


            if(songList.isEmpty()) songs.setText("Songs:...\nno results...");
            else songs.setText("Songs:...");
            this.searchSongRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            this.searchSongRecyclerView.setAdapter(new ListSongAdapter(songList, this));

        }
    }

    @Override
    public void onDataChangeResponse() {
        onDataArrivedResponse();
    }
}