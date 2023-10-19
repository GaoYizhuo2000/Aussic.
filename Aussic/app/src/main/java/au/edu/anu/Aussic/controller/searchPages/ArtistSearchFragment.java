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
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnGeneralItemClickListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataArrivedListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.entity.Artist;

/**
 * @author: u7516507, Evan Cheung
 */

/**
 * A simple {@link Fragment} subclass.
 * The filter of Artist results displaying
 */
public class ArtistSearchFragment extends Fragment implements OnDataArrivedListener, OnGeneralItemClickListener {
    private TextView artists;
    private RecyclerView searchArtistRecyclerView;

    public ArtistSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RuntimeObserver.addOnDataArrivedListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.artists = view.findViewById(R.id.search_artists_artists);
        this.searchArtistRecyclerView = view.findViewById(R.id.search_list_artist_artist_recyclerView);

        onDataArrivedResponse();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist_search, container, false);
    }

    @Override
    public void onDataArrivedResponse() {
        if(RuntimeObserver.getCurrentSongList() != null && !RuntimeObserver.getCurrentSongList().isEmpty()) {
            List<GeneralItem> artistList = new ArrayList<>();

            for (Artist artist : RuntimeObserver.currentSearchResultArtists)  artistList.add(new GeneralItem(artist));

            if(artistList.isEmpty()) artists.setText("Artists:...\nno results...");
            else artists.setText("Artists:...");
            this.searchArtistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            this.searchArtistRecyclerView.setAdapter(new ListArtistAdapter(artistList, this));

        }
    }
}