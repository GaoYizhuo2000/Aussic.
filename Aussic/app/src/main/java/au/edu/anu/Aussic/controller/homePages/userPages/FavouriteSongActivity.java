package au.edu.anu.Aussic.controller.homePages.userPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.Adapter.Functions;
import au.edu.anu.Aussic.controller.Runtime.Adapter.GeneralItem;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListFavSongAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnGeneralDeleteBtnClickListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataChangeListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.controller.entityPages.SongActivity;
import au.edu.anu.Aussic.models.GsonLoader.GsonLoader;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.parserAndTokenizer.Parser;
import au.edu.anu.Aussic.models.parserAndTokenizer.Tokenizer;

/**
 * @author: u7516507, Evan Cheung
 * @author: u7552399, Yizhuo Gao
 * @author: u7581818, Oscar Wei
 * @author: u7603590, Jiawei Niu
 */
public class FavouriteSongActivity extends AppCompatActivity implements OnGeneralDeleteBtnClickListener, OnDataChangeListener {
    /** RecyclerView for displaying the list of favorite songs. */
    private RecyclerView recyclerView;

    /** SearchView for searching favorite songs. */
    private SearchView searchView;

    /** Button to trigger the search functionality. */
    private Button searchButton;

    /** Button to navigate back to the user page. */
    private Button goBackUserPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_song);

        RuntimeObserver.addOnDataChangeListener(this);

        this.recyclerView = findViewById(R.id.favorites_search_list_recyclerView);

        this.searchView = findViewById(R.id.fav_searchview);
        this.searchButton = findViewById(R.id.fav_searchButton);
        this.goBackUserPage = findViewById(R.id.favorites_return_button);

        goBackUserPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        this.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request focus and show the keyboard
                searchView.setIconified(false);
            }
        });

        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String input) {
                // The IME action was detected, perform the doSearch
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
        onDataChangeResponse();
    }

    /**
     * Set up the RecyclerView with the provided list of favorite songs.
     *
     * @param favorites List of favorite songs.
     */
    private void setFavoritesList(List<Song> favorites){

        List<GeneralItem> itemList = new ArrayList<>();
        for(Song song : favorites) itemList.add(new GeneralItem(song));
        recyclerView.setLayoutManager(new LinearLayoutManager(FavouriteSongActivity.this));
        recyclerView.setAdapter(new ListFavSongAdapter(itemList, this));
    }

    /**
     * Handles click for each item in the RecyclerView.
     *
     * @param generalItem The clicked item's data.
     */
    @Override
    public void onItemClicked(GeneralItem generalItem) throws IOException {
        RuntimeObserver.setCurrentSong(generalItem.getSong());
        RuntimeObserver.getCurrentMediaPlayer().pause();
        RuntimeObserver.getCurrentMediaPlayer().release();
        RuntimeObserver.setMediaPlayer(new MediaPlayer());
        RuntimeObserver.getCurrentMediaPlayer().setDataSource(generalItem.getSong().getUrlToListen());
        RuntimeObserver.getCurrentMediaPlayer().prepare();
        RuntimeObserver.getCurrentMediaPlayer().setLooping(true);
        RuntimeObserver.getCurrentMediaPlayer().start();

        Intent intent = new Intent(FavouriteSongActivity.this, SongActivity.class);

        startActivity(intent);
    }

    /**
     * Refresh the list of songs based on current data state.
     */
    @Override
    public void onDataChangeResponse() {
        if(RuntimeObserver.currentUsrFavoriteSearchResults.isEmpty()){
            if(RuntimeObserver.currentUser.getFavorites().size() > RuntimeObserver.currentUsrFavoriteSongs.size()){

                FirestoreDao firestoreDao = new FirestoreDaoImpl();
                firestoreDao.getSongsByIdList(RuntimeObserver.currentUser.getFavorites()).thenAccept(results ->{
                    // Clear the currentUsrFavoriteSongs in runtime
                    RuntimeObserver.currentUsrFavoriteSongs = new ArrayList<>();

                    List<Map<String, Object>> maps = new ArrayList<>();
                    maps.addAll(results);
                    for(Map<String, Object> map : maps) {
                        Song newSong = GsonLoader.loadSong(map);

                        RuntimeObserver.currentUsrFavoriteSongs.add(newSong);

                    }
                    List<Song> favorites = RuntimeObserver.currentUsrFavoriteSongs;
                    setFavoritesList(favorites);
                });
            } else {
                List<Song> favorites = RuntimeObserver.currentUsrFavoriteSongs;
                setFavoritesList(favorites);
            }
        } else {
            List<Song> favorites = RuntimeObserver.currentUsrFavoriteSearchResults;
            setFavoritesList(favorites);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RuntimeObserver.currentUsrFavoriteSearchResults = new ArrayList<>();
    }

    /**
     * Deletes a song from favorites based on its position in the list.
     *
     * @param position Position of the song to be deleted.
     */
    @Override
    public void onDeleteBtnClicked(int position) {
        // Delete this song from firestore and runtimeObserver
        Song songDeleted = RuntimeObserver.currentUsrFavoriteSongs.remove(position);
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.deleteUserFavorites(songDeleted.getId());
        //delete this song from search engine
        RuntimeObserver.musicSearchEngine.deleteSong(songDeleted);
        //delete this song from view
        setFavoritesList(RuntimeObserver.currentUsrFavoriteSongs);
    }

    /**
     * Executes a search based on user input.
     *
     * @param input The search query.
     */
    private void doSearch(String input){
        if(input == null|| input.equals("")){
            RuntimeObserver.currentUsrFavoriteSearchResults = new ArrayList<>();
            RuntimeObserver.notifyOnDataChangeListeners();
            Functions.hideKeyboard(this);
            return;
        }
        Functions.hideKeyboard(this);
        // hide the keyboard when button is clicked
        searchView.setQuery("", false); // Set the query text to an empty string
        searchView.setIconified(true);
        searchView.setIconified(true);
        Parser parser = new Parser(new Tokenizer(input));
        Map<String, String> searchingTerms = parser.Parse();
        Set<Song> result = RuntimeObserver.musicSearchEngine.search(searchingTerms);

        if (!result.isEmpty()){
            RuntimeObserver.currentUsrFavoriteSearchResults = new ArrayList<>();
            RuntimeObserver.currentUsrFavoriteSearchResults.addAll(result);
            RuntimeObserver.notifyOnDataChangeListeners();
        }
    }
}