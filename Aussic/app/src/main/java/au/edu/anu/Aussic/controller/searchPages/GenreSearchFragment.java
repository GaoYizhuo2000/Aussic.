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
import au.edu.anu.Aussic.controller.Runtime.Adapter.GeneralItem;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnGeneralItemClickListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataArrivedListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.entity.Genre;

/**
 * @author: u7516507, Evan Cheung
 */

/**
 * A simple {@link Fragment} subclass.
 * The filter of Artist results displaying
 */
public class GenreSearchFragment extends Fragment implements OnDataArrivedListener, OnGeneralItemClickListener {
    private TextView genres;
    private RecyclerView searchGenreRecyclerView;

    public GenreSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RuntimeObserver.addOnDataArrivedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genre_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.genres = view.findViewById(R.id.search_genres_genres);
        this.searchGenreRecyclerView = view.findViewById(R.id.search_list_genre_genre_recyclerView);

        onDataArrivedResponse();
    }
    @Override
    public void onDataArrivedResponse() {
        if(RuntimeObserver.getCurrentSongList() != null && !RuntimeObserver.getCurrentSongList().isEmpty()) {
            List<GeneralItem> genreList = new ArrayList<>();

            for (Genre genre : RuntimeObserver.currentSearchResultGenres)  genreList.add(new GeneralItem(genre));

            if(genreList.isEmpty()) genres.setText("Genres:...\nno results...");
            else genres.setText("Genres:...");
            this.searchGenreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            this.searchGenreRecyclerView.setAdapter(new CardGenreAdapter(genreList, this));
        }
    }
}