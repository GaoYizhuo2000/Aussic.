package au.edu.anu.Aussic.controller.entityPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.Adapter.Functions;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ItemSpec;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListSongAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnItemSpecClickListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataArrivedListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.SongLoader.GsonLoader;
import au.edu.anu.Aussic.models.entity.Artist;
import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.parserAndTokenizer.Parser;
import au.edu.anu.Aussic.models.parserAndTokenizer.Tokenizer;

public class ArtistActivity extends AppCompatActivity implements OnDataArrivedListener, OnItemSpecClickListener {
    private ImageView imageView;
    private TextView name;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);

        RuntimeObserver.addOnDataArrivedListener(this);

        this.imageView = findViewById(R.id.artist_round_image);
        this.name = findViewById(R.id.artist_artist_name);
        this.recyclerView = findViewById(R.id.artist_recyclerview);

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

        List<ItemSpec> songList = new ArrayList<>();
        for(Song song : RuntimeObserver.songsUnderCurrentDisplayingArtist) songList.add(new ItemSpec(song));

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