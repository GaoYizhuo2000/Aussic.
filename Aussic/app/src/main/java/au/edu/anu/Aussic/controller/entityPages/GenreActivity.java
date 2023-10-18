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

public class GenreActivity extends AppCompatActivity implements OnDataArrivedListener, OnGeneralItemClickListener {
    private ImageView imageView;
    private TextView name;
    private RecyclerView recyclerView;
    private Button genreToHomeButton;

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

    @Override
    public void onDataArrivedResponse() {
        List<GeneralItem> songList = new ArrayList<>();
        for(Song song : RuntimeObserver.songsUnderCurrentDisplayingGenre) songList.add(new GeneralItem(song));

        recyclerView.setLayoutManager(new LinearLayoutManager(GenreActivity.this));
        recyclerView.setAdapter(new ListSongAdapter(songList, this));

        setImage(Functions.makeImageUrl(200, 200, RuntimeObserver.currentDisplayingGenre.getImageUrl()));
        this.name.setText(RuntimeObserver.currentDisplayingGenre.getGenreName());


    }

    private void setImage(String imageUrl){

        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().override((int)(720 * 0.8), (int)(720 * 0.8)))
                .into(this.imageView);
    }
}