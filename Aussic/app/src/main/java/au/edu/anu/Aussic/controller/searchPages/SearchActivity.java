package au.edu.anu.Aussic.controller.searchPages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

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
    private Button searchButton;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        FloatingActionButton fab = findViewById(R.id.search_fab);
        searchButton = findViewById(R.id.btn_search);
        searchView = findViewById(R.id.searchView_search);

        searchView.setInputType(InputType.TYPE_CLASS_TEXT);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request focus and show the soft keyboard
                if (searchView.isIconified()) searchView.setIconified(false);

            }
        });


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
                //把搜索框中的查询传给parser，在parser里转成map然后调用firestoreDao.searchSongs(terms)

                Map<String, Object> terms = new HashMap<>();
                terms.put("artistName", "INXS");
                terms.put("name", "Devil's Party (Slick Mix)");
                terms.put("releaseDate", null);




                FirestoreDao firestoreDao = new FirestoreDaoImpl();
                CompletableFuture<List<Map<String, Object>>> future = firestoreDao.searchSongs(terms);
                future.thenAccept(results -> {
                //拿到查询结果后处理结果，放入listview



                    String a = results.toString();




                });


            }
        });


    }
}