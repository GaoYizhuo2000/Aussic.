package au.edu.anu.Aussic.models.observer;

import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.models.entity.Song;

/**
 * The class for song playing and pausing
 */
public class MediaObserver {
    private static MediaPlayer currentMediaPlayer;
    private static Song currentSong;
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

    public static void addChangeListener(ChangeListener newListener) {
        listeners.add(newListener);
    }

    public static void removeChangeListener(ChangeListener listenerToRemove) {
        listeners.remove(listenerToRemove);
    }

    public static void notifyListeners() {
        for (ChangeListener listener : listeners) listener.onChange();
    }
}
