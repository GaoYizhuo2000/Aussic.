package au.edu.anu.Aussic.controller.entityPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
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

public class ArtistActivity extends AppCompatActivity implements OnDataArrivedListener, OnGeneralItemClickListener {
    /** ImageView for artist's picture */
    private ImageView imageView;

    /** TextView for artist's name */
    private TextView name;

    /** RecyclerView for displaying the songs of the artist */
    private RecyclerView recyclerView;

    /** Button for navigating back to home */
    private Button artistToHomeButton;

    /**
     * Lifecycle method called when activity is first created.
     * Initializes views and loads songs under the artist.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        RuntimeObserver.addOnDataArrivedListener(this);

        this.imageView = findViewById(R.id.artist_round_image);
        this.name = findViewById(R.id.artist_artist_name);
        this.recyclerView = findViewById(R.id.artist_recyclerview);
        this.artistToHomeButton = findViewById(R.id.artist_back_Home);

        artistToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Loading the songs under this artist when activity start
        loadSongsUnderArtist();
    }

    /**
     * Loads songs under the currently displayed artist.
     * Load the songs under certain artist using search engine
     */
    public void loadSongsUnderArtist(){

        // Get the songs of the artist from formatted searching
        String input = "\\a " + RuntimeObserver.currentDisplayingArtist.getArtistName();

        Parser parser = new Parser(new Tokenizer(input));
        Map<String, String> searchingTerms = parser.Parse();
        FirestoreDao firestoreDao = new FirestoreDaoImpl();

        firestoreDao.searchSongs(searchingTerms).thenAccept(results -> {

            RuntimeObserver.songsUnderCurrentDisplayingArtist = new ArrayList<>();

            List<Map<String, Object>> maps = new ArrayList<>();
            maps.addAll(results);
            for(Map<String, Object> map : maps) {
                if (map == null) continue;
                switch ((String) map.get("type")){
                    // Only add songs to the list
                    case "songs":
                        Song newSong = GsonLoader.loadSong(map);
                        RuntimeObserver.songsUnderCurrentDisplayingArtist.add(newSong);
                        break;
                }
            }
            // Response when data arrive
            onDataArrivedResponse();
        });
    }

    /**
     * Called when data has arrived.
     * Updates views with arrived data.
     */
    @Override
    public void onDataArrivedResponse() {
        // If data arrive, load data into list and image
        List<GeneralItem> songList = new ArrayList<>();
        for(Song song : RuntimeObserver.songsUnderCurrentDisplayingArtist) songList.add(new GeneralItem(song));

        recyclerView.setLayoutManager(new LinearLayoutManager(ArtistActivity.this));
        recyclerView.setAdapter(new ListSongAdapter(songList, this));

        setImage(Functions.makeImageUrl(200, 200, RuntimeObserver.currentDisplayingArtist.getImageUrl()));
        this.name.setText(RuntimeObserver.currentDisplayingArtist.getArtistName());

    }

    /**
     * Set the artist's image with the URL.
     *
     * @param imageUrl URL of the artist's image.
     */
    private void setImage(String imageUrl){

        // Set the image using Glide
        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().override((int)(720 * 0.8), (int)(720 * 0.8)))
                .circleCrop()
                .into(this.imageView);

        this.name.setText(RuntimeObserver.currentDisplayingArtist.getArtistName());
    }
}