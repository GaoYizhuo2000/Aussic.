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

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataArrivedListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.GsonLoader.GsonLoader;
import au.edu.anu.Aussic.models.entity.Artist;
import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.parserAndTokenizer.Parser;
import au.edu.anu.Aussic.models.parserAndTokenizer.Tokenizer;

public class SearchActivity extends AppCompatActivity implements OnDataArrivedListener {
    private MediaPlayer mediaPlayer;
    private Button searchButton;
    private SearchView searchView;
    private TabLayout tabs;
    private int sortID;
    private int fragmentID;
    private GeneralSearchFragment generalSearch;
    private SongSearchFragment songSearch;
    private ArtistSearchFragment artistSearch;
    private GenreSearchFragment genreSearch;
    private UserSearchFragment userSearch;
    private FloatingActionButton fab;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fab = findViewById(R.id.search_fab);
        searchButton = findViewById(R.id.btn_search);
        searchView = findViewById(R.id.searchView_search);

        this.generalSearch = new GeneralSearchFragment();
        this.songSearch = new SongSearchFragment();
        this.artistSearch = new ArtistSearchFragment();
        this.genreSearch = new GenreSearchFragment();
        this.userSearch = new UserSearchFragment();

        this.tabs = findViewById(R.id.tabs);
        String[] tabTitles = {"Likes", "Favorites", "General", "Song", "Artist", "Genre", "User"};

        for (String title : tabTitles) {
            tabs.addTab(tabs.newTab().setText(title));
        }


        for(int i = 0; i < 2; i++)  setTabColor(i, R.drawable.ic_tabs_trans_bg_alt);
        for(int i = 2; i < 7; i++)  setTabColor(i, R.drawable.ic_tabs_trans_bg);

        // Select general for the first init
        // Sort by Likes for the first init
        setTabColor(2, R.drawable.ic_tabs_bg);
        setTabColor(0, R.drawable.ic_tabs_bg_alt);
        this.fragmentID = 2;
        this.sortID = 0;
        replaceFragment(generalSearch);


        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int selectedTabIndex = tab.getPosition();
                if(selectedTabIndex > 1) {
                    setTabColor(fragmentID, R.drawable.ic_tabs_trans_bg);
                    fragmentID = selectedTabIndex;
                }
                else {
                    setTabColor(sortID, R.drawable.ic_tabs_trans_bg_alt);
                    sortID = selectedTabIndex;
                }
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
                    case 6:
                        setTabColor(6, R.drawable.ic_tabs_bg);
                        replaceFragment(userSearch);
                    // Add more cases if there are more tabs
                }
                if(selectedTabIndex > 3){
                    View likes = ((ViewGroup) tabs.getChildAt(0)).getChildAt(0);
                    View comments = ((ViewGroup) tabs.getChildAt(0)).getChildAt(1);
                    likes.setEnabled(false);
                    likes.setVisibility(View.GONE);
                    comments.setEnabled(false);
                    comments.setVisibility(View.GONE);
                    sortID = -1;
                } else {
                    View likes = ((ViewGroup) tabs.getChildAt(0)).getChildAt(0);
                    View comments = ((ViewGroup) tabs.getChildAt(0)).getChildAt(1);
                    likes.setEnabled(true);
                    likes.setVisibility(View.VISIBLE);
                    comments.setEnabled(true);
                    comments.setVisibility(View.VISIBLE);
                }

                if(sortID == 0) sortByLikes(RuntimeObserver.currentSearchResultSongs);
                else if(sortID == 1) sortByFavorites(RuntimeObserver.currentSearchResultSongs);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

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

        RuntimeObserver.addOnDataArrivedListener(this);

        this.mediaPlayer = RuntimeObserver.getCurrentMediaPlayer();

        if(mediaPlayer != null) {
            if (this.mediaPlayer.isPlaying()) fab.setImageResource(R.drawable.ic_bottom_stop);
            else fab.setImageResource(R.drawable.ic_bottom_play);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        fab.setImageResource(R.drawable.ic_bottom_play);
                    } else {
                        mediaPlayer.start();
                        fab.setImageResource(R.drawable.ic_bottom_stop);
                    }
                    RuntimeObserver.notifyOnMediaChangeListeners();
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
        //input eg: \a Troye; \nStrawberries and cigarettes
        //check if empty input
        if(input == null|| input.equals("")){
            return;
        }
        // hide the keyboard when button is clicked
        searchView.setQuery("", false); // Set the query text to an empty string
        searchView.setIconified(true);
        searchView.setIconified(true);

        Parser parser = new Parser(new Tokenizer(input));
        Map<String, String> searchingTerms = parser.Parse();
        FirestoreDao firestoreDao = new FirestoreDaoImpl();


        firestoreDao.searchSongs(searchingTerms).thenAccept(results -> {
            //拿到查询结果后处理，放入listview展示 results是歌曲列表
            RuntimeObserver.currentSearchResultSongs = new ArrayList<>();
            RuntimeObserver.currentSearchResultArtists = new ArrayList<>();
            RuntimeObserver.currentSearchResultGenres = new ArrayList<>();

            List<Map<String, Object>> maps = new ArrayList<>();
            maps.addAll(results); //结果里会有歌和genre和artist的对象json（用general 搜索的话），先检查type参数再分别处理
            for(Map<String, Object> map : maps) {
                if (map == null) continue;
                switch ((String) map.get("type")){
                    case "songs":
                        Song newSong = GsonLoader.loadSong(map);
                        firestoreDao.setSongRealTimeListener(newSong);
                        RuntimeObserver.currentSearchResultSongs.add(newSong);
                        break;
                    case "artists":
                        Artist newArtist = GsonLoader.loadArtist(map);
                        RuntimeObserver.currentSearchResultArtists.add(newArtist);
                        break;
                    case "genres":
                        Genre newGenre = GsonLoader.loadGenre(map);
                        RuntimeObserver.currentSearchResultGenres.add(newGenre);
                        break;
                    case "users":
                        if (map.get("username").equals(RuntimeObserver.currentUser.username)) continue;
                        User newUsr = GsonLoader.loadUser(map);
                        RuntimeObserver.currentSearchResultUsers.add(newUsr);
                        break;
                }
                if(sortID == 0) sortByLikes(RuntimeObserver.currentSearchResultSongs);
                else if(sortID == 1) sortByFavorites(RuntimeObserver.currentSearchResultSongs);


            }
            RuntimeObserver.notifyOnDataArrivedListeners();

//            results.toString();
//            System.out.println(results);
//            Toast.makeText(this, results.toString(), Toast.LENGTH_LONG).show();

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

    @Override
    public void onDataArrivedResponse(){
        this.mediaPlayer = RuntimeObserver.getCurrentMediaPlayer();
        if (mediaPlayer.isPlaying()) this.fab.setImageResource(R.drawable.ic_bottom_stop);
        else this.fab.setImageResource(R.drawable.ic_bottom_play);
    }


    private void sortByLikes(List<Song> songList){
        Collections.sort(songList, new Comparator<Song>(){
            @Override
            public int compare(Song s1, Song s2) {
                return s2.getLikes() - s1.getLikes();
            }
        });
        RuntimeObserver.notifyOnDataChangeListeners();
    }

    private void sortByFavorites(List<Song> songList){
        Collections.sort(songList, new Comparator<Song>(){
            @Override
            public int compare(Song s1, Song s2) {
                return s2.getFavorites() - s1.getFavorites();
            }
        });
        RuntimeObserver.notifyOnDataChangeListeners();
    }
}