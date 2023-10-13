package au.edu.anu.Aussic.controller.observer;

import android.media.MediaPlayer;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.controller.homePages.HomeActivity;
import au.edu.anu.Aussic.controller.homePages.HomeFragment;
import au.edu.anu.Aussic.models.SongLoader.GsonSongLoader;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

/**
 * The class for song playing and pausing
 */
public class RuntimeObserver {
    private static MediaPlayer currentMediaPlayer;
    private static Song currentSong;
    public static User currentUser;
    public static ImageView roundImage;
    public static HomeFragment homeFragment;
    public static HomeActivity homeActivity;
    private static List<Song> currentSongList = new ArrayList<>();
    public static List<Song> currentSearchResultSongs;
    private static List<OnDataArrivedListener> onDataArrivedListeners = new ArrayList<>();
    private static List<OnDataChangeListener> onDataChangeListeners = new ArrayList<>();
    public static void setMediaPlayer(MediaPlayer mediaPlayer){
        currentMediaPlayer = mediaPlayer;
        notifyOnDataArrivedListener();
    }
    public static void setCurrentSong(Song song){
        currentSong = song;
    }

    public static MediaPlayer getCurrentMediaPlayer() {
        return currentMediaPlayer;
    }

    public static Song getCurrentSong() {
        return currentSong;
    }
    public static List<Song> getCurrentSongList(){return currentSongList; }

    public static void addOnDataArrivedListener(OnDataArrivedListener newListener) {
        onDataArrivedListeners.add(newListener);
    }
    public static void addOnDataChangeListeners(OnDataChangeListener newListener) {
        onDataChangeListeners.add(newListener);
    }
    public static void notifyOnDataArrivedListener() {
        for (OnDataArrivedListener listener : onDataArrivedListeners) listener.onChange();
    }
    public static void notifyOnDataChangeListeners(){
        for(OnDataChangeListener listener : onDataChangeListeners) listener.onDataChangeResponse();
    }

    public static void setSongRealTimeListener(Song song){
        FirestoreDao firestoreDao = new FirestoreDaoImpl();

        DocumentReference songRef = firestoreDao.getSongsRef().document(song.getId());
        songRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    song.setSong(GsonSongLoader.loadSong(snapshot.getData()));
                    // Notify subsequent change
                    notifyOnDataChangeListeners();
                }
            }
        });
    }

    public static void setUsrRealTimeListener(User usr){
        FirestoreDao firestoreDao = new FirestoreDaoImpl();

        DocumentReference usrRef = firestoreDao.getUsrRef().document(usr.username);
        usrRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    Map<String, Object> usrData = snapshot.getData();
                    String iconUrl = (String) usrData.get("iconUrl");
                    String usrName = (String) usrData.get("username");

                    User newUsr = new User(usrName, iconUrl);

                    for(String songID : (List<String>)usrData.get("favorites")) newUsr.addFavorites(songID);
                    for(String songID : (List<String>)usrData.get("likes")) newUsr.addLikes(songID);

                    usr.setUsr(newUsr);
                    // Notify subsequent change
                    notifyOnDataChangeListeners();
                }
            }
        });
    }
}
