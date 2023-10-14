package au.edu.anu.Aussic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.List;

import au.edu.anu.Aussic.controller.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

public class FavouriteSongList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_song_list);
        List<String> favorites = RuntimeObserver.currentUser.getFavorites();
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.getSongsByIdList(favorites).thenAccept(results ->{
            results.toString();
            results.toString();
            //放到list里展示


        });



    }
}