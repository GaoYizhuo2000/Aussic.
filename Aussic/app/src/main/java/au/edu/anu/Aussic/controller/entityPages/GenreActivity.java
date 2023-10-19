package au.edu.anu.Aussic.controller.entityPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.Adapter.Functions;
import au.edu.anu.Aussic.controller.Runtime.Adapter.GeneralItem;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListSongAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnGeneralItemClickListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataArrivedListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.GsonLoader.GsonLoader;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.parserAndTokenizer.Parser;
import au.edu.anu.Aussic.models.parserAndTokenizer.Tokenizer;

/**
 * @author: u7516507, Evan Cheung
 * @author: u7603590, Jiawei Niu
 */

public class GenreActivity extends AppCompatActivity implements OnDataArrivedListener, OnGeneralItemClickListener {
    /** ImageView for genre's representation */
    private ImageView imageView;

    /** TextView for genre's name */
    private TextView name;

    /** RecyclerView for displaying the songs of the genre */
    private RecyclerView recyclerView;

    /** Button for navigating back to home */
    private Button genreToHomeButton;

    /**
     * Lifecycle method called when activity is first created.
     * Initializes views and loads songs under the genre.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);

        RuntimeObserver.addOnDataArrivedListener(this);

        this.imageView = findViewById(R.id.genre_image);
        this.name = findViewById(R.id.genre_genre_name);
        this.recyclerView = findViewById(R.id.genre_recyclerview);
        this.genreToHomeButton = findViewById(R.id.genre_back_Home);

        genreToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadSongsUnderGenre();
    }

    /**
     * Loads songs under currently displayed genre.
     * Load the songs under certain genre using search engine
     */
    public void loadSongsUnderGenre(){

        String input = "\\g " + RuntimeObserver.currentDisplayingGenre.getGenreName();

        Parser parser = new Parser(new Tokenizer(input));
        Map<String, String> searchingTerms = parser.Parse();
        FirestoreDao firestoreDao = new FirestoreDaoImpl();

        firestoreDao.searchSongs(searchingTerms).thenAccept(results -> {

            RuntimeObserver.songsUnderCurrentDisplayingGenre = new ArrayList<>();

            List<Map<String, Object>> maps = new ArrayList<>();
            maps.addAll(results);
            for(Map<String, Object> map : maps) {
                if (map == null) continue;
                switch ((String) map.get("type")){
                    case "songs":
                        Song newSong = GsonLoader.loadSong(map);
                        RuntimeObserver.songsUnderCurrentDisplayingGenre.add(newSong);
                        break;
                }
            }
            onDataArrivedResponse();
        });

    }

    /**
     * Called when song data for the genre has arrived.
     * Updates views with arrived data.
     */
    @Override
    public void onDataArrivedResponse() {
        List<GeneralItem> songList = new ArrayList<>();
        for(Song song : RuntimeObserver.songsUnderCurrentDisplayingGenre) songList.add(new GeneralItem(song));

        recyclerView.setLayoutManager(new LinearLayoutManager(GenreActivity.this));
        recyclerView.setAdapter(new ListSongAdapter(songList, this));

        setImage(Functions.makeImageUrl(200, 200, RuntimeObserver.currentDisplayingGenre.getImageUrl()));
        this.name.setText(RuntimeObserver.currentDisplayingGenre.getGenreName());

    }

    /**
     * Set the genre's representation image using the provided URL.
     *
     * @param imageUrl URL of the genre's image.
     */
    private void setImage(String imageUrl){

        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().override((int)(720 * 0.8), (int)(720 * 0.8)))
                .into(this.imageView);
    }
}