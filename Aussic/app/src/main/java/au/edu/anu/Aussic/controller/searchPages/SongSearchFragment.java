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
import au.edu.anu.Aussic.controller.Runtime.Adapter.CardGenreAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ItemSpec;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListArtistAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListSongAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnItemSpecClickListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataArrivedListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataChangeListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.entity.Artist;
import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.Song;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SongSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongSearchFragment extends Fragment implements OnDataArrivedListener, OnDataChangeListener, OnItemSpecClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView songs;
    private RecyclerView searchSongRecyclerView;

    public SongSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongNameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongSearchFragment newInstance(String param1, String param2) {
        SongSearchFragment fragment = new SongSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
            List<ItemSpec> songList = new ArrayList<>();

            for (Song song : RuntimeObserver.currentSearchResultSongs)  songList.add(new ItemSpec(song));


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