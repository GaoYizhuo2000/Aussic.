package au.edu.anu.Aussic.controller.searchPages;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.models.entity.Media;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

public class SearchActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        FloatingActionButton fab = findViewById(R.id.search_fab);
        Button searchButton = findViewById(R.id.btn_search);

        this.mediaPlayer = Media.mediaPlayer;
        if(this.mediaPlayer.isPlaying()) fab.setImageResource(R.drawable.ic_bottom_stop);
        else fab.setImageResource(R.drawable.ic_bottom_play);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    fab.setImageResource(R.drawable.ic_bottom_play);
                }
                else {
                    mediaPlayer.start();
                    fab.setImageResource(R.drawable.ic_bottom_stop);
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> terms = new HashMap<>();
                terms.put("artistName", "INXS");
                terms.put("name", "Devil's Party (Slick Mix)");
                terms.put("releaseDate", null);
                FirestoreDao firestoreDao = new FirestoreDaoImpl();
                CompletableFuture<List<Map<String, Object>>> future = firestoreDao.searchSongs(terms);
                future.thenAccept(results -> {


                    System.out.println(results);



                });


            }
        });


    }
}