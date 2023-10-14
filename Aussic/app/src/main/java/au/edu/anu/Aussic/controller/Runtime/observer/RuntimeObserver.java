package au.edu.anu.Aussic.controller.Runtime.observer;

import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;

/**
 * The class for song playing and pausing
 */
public class RuntimeObserver {
    private static MediaPlayer currentMediaPlayer;
    private static Song currentSong;
    public static User currentUser;
    private static List<Song> currentSongList = new ArrayList<>();
    public static List<Song> currentSearchResultSongs;
    private static List<OnDataArrivedListener> onDataArrivedListeners = new ArrayList<>();
    private static List<OnDataChangeListener> onDataChangeListeners = new ArrayList<>();
    private static List<OnMediaChangeListener> onMediaChangeListeners = new ArrayList<>();
    public static void setMediaPlayer(MediaPlayer mediaPlayer){
        currentMediaPlayer = mediaPlayer;
        notifyOnDataArrivedListeners();
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
    public static void addOnDataChangeListener(OnDataChangeListener newListener) {
        onDataChangeListeners.add(newListener);
    }
    public static void addOnMediaChangeListener(OnMediaChangeListener newListener) {
        onMediaChangeListeners.add(newListener);
    }
    public static void notifyOnDataArrivedListeners() {
        for (OnDataArrivedListener listener : onDataArrivedListeners) listener.onDataArrivedResponse();
    }
    public static void notifyOnDataChangeListeners(){
        for(OnDataChangeListener listener : onDataChangeListeners) listener.onDataChangeResponse();
    }
    public static void notifyOnMediaChangeListeners(){
        for(OnMediaChangeListener listener : onMediaChangeListeners) listener.onMediaChangeResponse();
    }

}
