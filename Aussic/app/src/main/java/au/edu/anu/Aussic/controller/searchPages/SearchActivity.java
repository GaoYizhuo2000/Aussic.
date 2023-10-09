package au.edu.anu.Aussic.controller.searchPages;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.models.entity.Media;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

public class SearchActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button searchButton;
    private SearchView searchView;
    private TabLayout tabs;
    private int selectID;
    private boolean isInit;
    private GeneralSearchFragment generalSearch;
    private SongSearchFragment songSearch;
    private ArtistSearchFragment artistSearch;
    private GenreSearchFragment genreSearch;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        FloatingActionButton fab = findViewById(R.id.search_fab);
        searchButton = findViewById(R.id.btn_search);
        searchView = findViewById(R.id.searchView_search);

        this.generalSearch = new GeneralSearchFragment();
        this.songSearch = new SongSearchFragment();
        this.artistSearch = new ArtistSearchFragment();
        this.genreSearch = new GenreSearchFragment();

        this.tabs = findViewById(R.id.tabs);

        for(int i = 0; i < 2; i++)  setTabColor(i, R.drawable.ic_tabs_trans_bg_alt);
        for(int i = 2; i < 6; i++)  setTabColor(i, R.drawable.ic_tabs_trans_bg);

        // Select general for the first init
        setTabColor(2, R.drawable.ic_tabs_bg);
        this.selectID = 2;
        this.isInit = true;
        replaceFragment(generalSearch);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(isInit) {
                    setTabColor(2, R.drawable.ic_tabs_trans_bg);
                    isInit = false;
                }
                int selectedTabIndex = tab.getPosition();
                switch (selectedTabIndex) {
                    case 0:
                        // Actions for "Likes" tab
                        setTabColor(0, R.drawable.ic_tabs_bg_alt);
                        break;
                    case 1:
                        // Actions for "Comments" tab
                        setTabColor(1, R.drawable.ic_tabs_bg_alt);
                        break;
                    case 2:
                        // Actions for "General" tab
                        setTabColor(2, R.drawable.ic_tabs_bg);
                        replaceFragment(generalSearch);
                        break;
                    case 3:
                        setTabColor(3, R.drawable.ic_tabs_bg);
                        replaceFragment(songSearch);
                        // Actions for "Song" tab
                        break;
                    case 4:
                        setTabColor(4, R.drawable.ic_tabs_bg);
                        replaceFragment(artistSearch);
                        // Actions for "Artist" tab
                        break;
                    case 5:
                        setTabColor(5, R.drawable.ic_tabs_bg);
                        replaceFragment(genreSearch);
                        // Actions for "Genre" tab
                        break;
                    // Add more cases if there are more tabs
                }
                selectID = selectedTabIndex;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int selectedTabIndex = tab.getPosition();
                switch (selectedTabIndex) {
                    case 0:
                        // Actions for "Likes" tab
                        setTabColor(0, R.drawable.ic_tabs_trans_bg_alt);
                        break;
                    case 1:
                        // Actions for "Comments" tab
                        setTabColor(1, R.drawable.ic_tabs_trans_bg_alt);
                        break;
                    case 2:
                        // Actions for "General" tab
                        setTabColor(2, R.drawable.ic_tabs_trans_bg);
                        break;
                    case 3:
                        setTabColor(3, R.drawable.ic_tabs_trans_bg);
                        // Actions for "Song" tab
                        break;
                    case 4:
                        setTabColor(4, R.drawable.ic_tabs_trans_bg);
                        // Actions for "Artist" tab
                        break;
                    case 5:
                        setTabColor(5, R.drawable.ic_tabs_trans_bg);
                        // Actions for "Genre" tab
                        break;
                    // Add more cases if there are more tabs
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab){}
        });


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request focus and show the soft keyboard
                searchView.setIconified(false);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String input) {
                // The IME action was detected, perform the same action as your search button
                doSearch(input);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle real-time query changes if needed
                return false;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchView.getQuery().toString();
                doSearch(query);
            }
        });


    }

    private void doSearch(String input){
        // hide the keyboard when button is clicked
        searchView.setQuery("", false); // Set the query text to an empty string
        searchView.setIconified(true);
        searchView.setIconified(true);

        //把搜索框中的查询传给parser，在parser里转成map然后调用firestoreDao.searchSongs(terms)

        Map<String, Object> terms = new HashMap<>();
        terms.put("artistName", "INXS");
        terms.put("name", "Devil's Party (Slick Mix)");
        terms.put("releaseDate", null);


        FirestoreDao firestoreDao = new FirestoreDaoImpl();
//        firestoreDao.searchSongs(terms).thenAccept(results -> {
//            //拿到查询结果后处理，放入listview
//
//        });

        List<Map<String, Object>> songs = new ArrayList<>();
        //String a = firestoreDao.getRandomSong().toString();
        firestoreDao.getRandomSongs(100).thenAccept(results ->{

            songs.addAll(results);

            Object attributes = songs.get(0).get("attributes");
            Object artwork = ((Map<String, Object>) attributes).get("artwork");
            String urlToImage = ((Map<String, Object>) artwork).get("url").toString();
            Map<String, Object> a = songs.get(0);
            Gson gson = new Gson();
            String jsonData = gson.toJson(a);
//            Song song = gson.fromJson(jsonData, Song.class);

//            Song song = new Song(songs.get(0));

            Toast.makeText(this, urlToImage, Toast.LENGTH_SHORT).show();
        });


        

    }

    private void setTabColor(int index, int drawable) {
        View tabView = ((ViewGroup) tabs.getChildAt(0)).getChildAt(index);

        Drawable background = tabView.getBackground();

        RippleDrawable oldRippleDrawable = (RippleDrawable) background;

        // Extract ripple color from the old ripple drawable
        ColorStateList rippleColor = oldRippleDrawable.getEffectColor();

        // Create new RippleDrawable
        RippleDrawable newRippleDrawable = new RippleDrawable(rippleColor, getResources().getDrawable(drawable), null);

        // Set the new background
        tabView.setBackground(newRippleDrawable);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }
}