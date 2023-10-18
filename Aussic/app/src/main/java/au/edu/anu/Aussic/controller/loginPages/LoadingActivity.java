package au.edu.anu.Aussic.controller.loginPages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.controller.homePages.HomeActivity;
import au.edu.anu.Aussic.models.GsonLoader.GsonLoader;
import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.Session;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.search.MusicSearchEngine;

/**
 * @author: u7516507, Evan Cheung
 */

public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        loadUsrData(user);
    }


    private void loadUsrData(FirebaseUser user){
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.getUserdata(user)
                .thenAccept(userdata -> {
                    au.edu.anu.Aussic.models.entity.User newUsr = GsonLoader.loadUser(userdata);

                    // Set up real time listener for user
                    firestoreDao.setUsrRealTimeListener(newUsr);

                    RuntimeObserver.currentUser = newUsr;

                    // If empty, jump the loading of usr fav songs
                    if(!newUsr.getFavorites().isEmpty()){
                        loadUsrFavSongs();
                    }else {
                        loadPlayingSong();
                    }
                });

    }


    private void loadUsrFavSongs(){
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.getSongsByIdList(RuntimeObserver.currentUser.getFavorites()).thenAccept(results ->{
            List<Map<String, Object>> maps = new ArrayList<>();
            maps.addAll(results);
            for(Map<String, Object> map : maps) {
                Song newSong = GsonLoader.loadSong(map);

                RuntimeObserver.currentUsrFavoriteSongs.add(newSong);

            }
            RuntimeObserver.musicSearchEngine = new MusicSearchEngine(RuntimeObserver.currentUsrFavoriteSongs);
            loadPlayingSong();
        });
    }

    private void loadPlayingSong(){
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.getRandomSongs(1).thenAccept(results ->{
            Song newSong = GsonLoader.loadSong(results.get(0));
            RuntimeObserver.setCurrentSong(newSong);

            // Setup realtime listener for the song
            firestoreDao.setSongRealTimeListener(newSong);

            RuntimeObserver.setMediaPlayer(new MediaPlayer());

            try{
                RuntimeObserver.getCurrentMediaPlayer().setDataSource(RuntimeObserver.getCurrentSong().getUrlToListen());
                RuntimeObserver.getCurrentMediaPlayer().prepare();
                RuntimeObserver.getCurrentMediaPlayer().setLooping(true);
                //mediaPlayer.setVolume(1.0f,1.0f);
            } catch (Exception e){
                e.printStackTrace();
            }
            //RuntimeObserver.getCurrentMediaPlayer().start();
            //RuntimeObserver.notifyListeners();
            loadDisplayingSongs(10);
        });
    }

    private void loadDisplayingSongs(int num){
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.getRandomSongs(num).thenAccept(results->{
            List<Map<String, Object>> maps = new ArrayList<>();
            maps.addAll(results);
            for(Map<String, Object> map : maps) {
                Song newSong = GsonLoader.loadSong(map);

                RuntimeObserver.getCurrentSongList().add(newSong);
            }
            loadDisplayingGenres(5);
        });
    }

    private void loadDisplayingGenres(int num){
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.loadRandomGenres(num).thenAccept(results->{
            List<Map<String, Object>> maps = new ArrayList<>();
            maps.addAll(results);
            for(Map<String, Object> map : maps) {
                Genre newGenre = GsonLoader.loadGenre(map);

                RuntimeObserver.currentGenreList.add(newGenre);
            }
            // Set the real time listener for session collection
            firestoreDao.setSessionCollectionRealtimeListener();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        });

    }
}