package au.edu.anu.Aussic.controller;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
                DocumentReference songRef = firestoreDao.getSongsRef().document(newSong.getId());
                songRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            newSong.setSong(GsonSongLoader.loadSong(snapshot.getData()));
                            snapshot.getData();
                            Map<String, Object> comments = (Map<String, Object>) snapshot.get("comments");
                            List<Map<String, Object>> details = (List<Map<String, Object>>) comments.get("details");


                            // Update UI with new comments
//                            updateCommentsUI(comments);
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });


                RuntimeObserver.getCurrentSongList().add(newSong);
            }

            //setViewList(maps);
            //RuntimeObserver.notifyListeners();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
}