package au.edu.anu.Aussic.controller.homePages.userPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ItemSpec;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListFavSongAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnDeleteBtnClickListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataChangeListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.controller.entityPages.SongActivity;
import au.edu.anu.Aussic.controller.loginPages.LoginActivity;
import au.edu.anu.Aussic.controller.loginPages.SignUpActivity;
import au.edu.anu.Aussic.models.SongLoader.GsonSongLoader;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;


public class FavouriteSongActivity extends AppCompatActivity implements OnDeleteBtnClickListener, OnDataChangeListener {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private Button searchButton, goBackUserPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_song);

        RuntimeObserver.addOnDataChangeListener(this);

        this.recyclerView = findViewById(R.id.favorites_search_list_recyclerView);

        this.searchView = findViewById(R.id.fav_searchview);
        this.searchButton = findViewById(R.id.fav_searchButton);
        goBackUserPage = findViewById(R.id.favorites_return_button);

        goBackUserPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        this.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request focus and show the soft keyboard
                searchView.setIconified(false);
            }
        });

        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        onDataChangeResponse();
    }

    private void setFavoritesList(List<Song> favorites){
        List<ItemSpec> itemList = new ArrayList<>();
        for(Song song : favorites) itemList.add(new ItemSpec(song));
        recyclerView.setLayoutManager(new LinearLayoutManager(FavouriteSongActivity.this));
        recyclerView.setAdapter(new ListFavSongAdapter(itemList, this));
    }

    @Override
    public void onItemClicked(ItemSpec itemSpec) throws IOException {
        RuntimeObserver.setCurrentSong(itemSpec.getSong());
        RuntimeObserver.getCurrentMediaPlayer().pause();
        RuntimeObserver.getCurrentMediaPlayer().release();
        RuntimeObserver.setMediaPlayer(new MediaPlayer());
        RuntimeObserver.getCurrentMediaPlayer().setDataSource(itemSpec.getSong().getUrlToListen());
        RuntimeObserver.getCurrentMediaPlayer().prepare();
        RuntimeObserver.getCurrentMediaPlayer().setLooping(true);
        RuntimeObserver.getCurrentMediaPlayer().start();

        Intent intent = new Intent(FavouriteSongActivity.this, SongActivity.class);
        // add more extras if necessary
        startActivity(intent);
    }

    @Override
    public void onDataChangeResponse() {
        if(RuntimeObserver.currentUser.getFavorites().size() > RuntimeObserver.currentUsrFavoriteSongs.size()){

            FirestoreDao firestoreDao = new FirestoreDaoImpl();
            firestoreDao.getSongsByIdList(RuntimeObserver.currentUser.getFavorites()).thenAccept(results ->{
                // Clear the currentUsrFavoriteSongs in runtime
                RuntimeObserver.currentUsrFavoriteSongs = new ArrayList<>();

                List<Map<String, Object>> maps = new ArrayList<>();
                maps.addAll(results);
                for(Map<String, Object> map : maps) {
                    Song newSong = GsonSongLoader.loadSong(map);

                    RuntimeObserver.currentUsrFavoriteSongs.add(newSong);

                    firestoreDao.setSongRealTimeListener(newSong);
                }
                List<Song> favorites = RuntimeObserver.currentUsrFavoriteSongs;
                setFavoritesList(favorites);
            });
        } else {
            List<Song> favorites = RuntimeObserver.currentUsrFavoriteSongs;
            setFavoritesList(favorites);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDeleteBtnClicked(int position) {

        // Add delete event here
        //delete this song from firestore
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
      //  firestoreDao.deleteUserFavorites();
        //delete this song from search engine
        //RuntimeObserver.musicSearchEngine.

        //delete this song from view

    }

    private void doSearch(String query){

    }
}