package au.edu.anu.Aussic.controller.observer;

import android.media.MediaPlayer;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.controller.homePages.HomeActivity;
import au.edu.anu.Aussic.controller.homePages.HomeFragment;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;

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
    private static List<ChangeListener> listeners = new ArrayList<>();
    public static void setMediaPlayer(MediaPlayer mediaPlayer){
        currentMediaPlayer = mediaPlayer;
        notifyListeners();
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

    public static void addChangeListener(ChangeListener newListener) {
        listeners.add(newListener);
    }

    public static void removeChangeListener(ChangeListener listenerToRemove) {
        listeners.remove(listenerToRemove);
    }

    public static void notifyListeners() {
        for (ChangeListener listener : listeners) listener.onChange();
    }
    public static void realTimeNotifyListeners(){

    }
}
