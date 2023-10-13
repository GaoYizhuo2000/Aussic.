package au.edu.anu.Aussic.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.homePages.Adapter.CommentItem;
import au.edu.anu.Aussic.controller.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.SongLoader.GsonSongLoader;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        loadPlayingSong();

    }

    private void loadPlayingSong(){
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.getRandomSongs(1).thenAccept(results ->{
            Song newSong = GsonSongLoader.loadSong(results.get(0));
            RuntimeObserver.setCurrentSong(newSong);
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
            loadDisplayingSongs();
        });
    }

    private void loadDisplayingSongs(){
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.getRandomSongs(10).thenAccept(results->{
            List<Map<String, Object>> maps = new ArrayList<>();
            maps.addAll(results);
            for(Map<String, Object> map : maps) {
                Song newSong = GsonSongLoader.loadSong(map);

                RuntimeObserver.getCurrentSongList().add(newSong);
            }

            //setViewList(maps);
            //RuntimeObserver.notifyListeners();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
}