package au.edu.anu.Aussic.models.userAction;

import com.google.firebase.firestore.FirebaseFirestore;

import au.edu.anu.Aussic.models.firestoreSingleton.Firestore;

public class Favorites extends UserAction{


    public Favorites(String actionType, String username, String targetSong, Integer targetSongId) {
        super(actionType, username, targetSong, targetSongId);
    }

    @Override
    public String getToastMessage() {
        return String.format("User %s added the song \"%s\" to favorites", username, targetSong);
    }

    @Override
    public void update() {
        FirebaseFirestore db = Firestore.getInstance();
        System.out.println(db + "");
    }
}
