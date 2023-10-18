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
    private ImageView imageView;
    private TextView name;
    private RecyclerView recyclerView;
    private Button artistToHomeButton;
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

        loadSongsUnderArtist();
    }

    public void loadSongsUnderArtist(){

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
                    case "songs":
                        Song newSong = GsonLoader.loadSong(map);
                        RuntimeObserver.songsUnderCurrentDisplayingArtist.add(newSong);
                        break;
                }
            }
            onDataArrivedResponse();
        });
    }

    @Override
    public void onDataArrivedResponse() {

        List<GeneralItem> songList = new ArrayList<>();
        for(Song song : RuntimeObserver.songsUnderCurrentDisplayingArtist) songList.add(new GeneralItem(song));

        recyclerView.setLayoutManager(new LinearLayoutManager(ArtistActivity.this));
        recyclerView.setAdapter(new ListSongAdapter(songList, this));

        setImage(Functions.makeImageUrl(200, 200, RuntimeObserver.currentDisplayingArtist.getImageUrl()));
        this.name.setText(RuntimeObserver.currentDisplayingArtist.getArtistName());

    }

    private void setImage(String imageUrl){

        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().override((int)(720 * 0.8), (int)(720 * 0.8)))
                .circleCrop()
                .into(this.imageView);

        this.name.setText(RuntimeObserver.currentDisplayingArtist.getArtistName());
    }
}