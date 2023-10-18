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
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListArtistAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.CardGenreAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListSongAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListUsrAdapter;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataChangeListener;
import au.edu.anu.Aussic.models.entity.Artist;
import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataArrivedListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnGeneralItemClickListener;
import au.edu.anu.Aussic.models.entity.User;

/**
 * @author: u7516507, Evan Cheung
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneralSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralSearchFragment extends Fragment implements OnDataArrivedListener, OnDataChangeListener, OnGeneralItemClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private TextView songs;
    private RecyclerView searchSongRecyclerView;
    private TextView artists;
    private RecyclerView searchArtistRecyclerView;
    private TextView genres;
    private RecyclerView searchGenreRecyclerView;
    private TextView users;
    private RecyclerView searchUserRecyclerView;

    public GeneralSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllTypesFragment.
     */

    public static GeneralSearchFragment newInstance(String param1, String param2) {
        GeneralSearchFragment fragment = new GeneralSearchFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.songs = view.findViewById(R.id.search_general_songs);
        this.searchSongRecyclerView = view.findViewById(R.id.search_list_song_recyclerView);

        this.artists = view.findViewById(R.id.search_general_artists);
        this.searchArtistRecyclerView = view.findViewById(R.id.search_list_artist_recyclerView);

        this.genres = view.findViewById(R.id.search_general_genres);
        this.searchGenreRecyclerView = view.findViewById(R.id.search_list_genre_recyclerView);

        this.users = view.findViewById(R.id.search_general_users);
        this.searchUserRecyclerView = view.findViewById(R.id.search_list_user_recyclerView);

        onDataArrivedResponse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general_search, container, false);
    }

    @Override
    public void onDataArrivedResponse() {
        if(RuntimeObserver.getCurrentSongList() != null && !RuntimeObserver.getCurrentSongList().isEmpty()) {
            List<GeneralItem> songList = new ArrayList<>();
            List<GeneralItem> artistList = new ArrayList<>();
            List<GeneralItem> genreList = new ArrayList<>();
            List<GeneralItem> userList = new ArrayList<>();

            if(RuntimeObserver.currentSearchResultSongs == null) for (Song song : RuntimeObserver.getCurrentSongList())  songList.add(new GeneralItem(song));
            else for (Song song : RuntimeObserver.currentSearchResultSongs)  songList.add(new GeneralItem(song));

            for (Genre genre : RuntimeObserver.currentSearchResultGenres)  genreList.add(new GeneralItem(genre));

            for (Artist artist : RuntimeObserver.currentSearchResultArtists)  artistList.add(new GeneralItem(artist));

            for (User user : RuntimeObserver.currentSearchResultUsers) userList.add(new GeneralItem(user));

            if(songList.isEmpty()) songs.setText("Songs:...\nno results...");
            else songs.setText("Songs:...");
            this.searchSongRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            this.searchSongRecyclerView.setAdapter(new ListSongAdapter(songList, this));

            if(genreList.isEmpty()) genres.setText("Genres:...\nno results...");
            else genres.setText("Genres:...");
            this.searchGenreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            this.searchGenreRecyclerView.setAdapter(new CardGenreAdapter(genreList, this));

            if(artistList.isEmpty()) artists.setText("Artists:...\nno results...");
            else artists.setText("Artists:...");
            this.searchArtistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            this.searchArtistRecyclerView.setAdapter(new ListArtistAdapter(artistList, this));

            if(userList.isEmpty()) users.setText("Users:...\nno results...");
            else users.setText("Users:...");
            this.searchUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            this.searchUserRecyclerView.setAdapter(new ListUsrAdapter(userList, this));
        }
    }

    @Override
    public void onDataChangeResponse() {
        onDataArrivedResponse();
    }
}