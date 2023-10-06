package au.edu.anu.Aussic.models.userAction;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;

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
        DocumentReference docRef = db.collection("songs").document(targetSongId + "");
        Map<String, Object> updates = new HashMap<>();
        updates.put("favorites", Integer.parseInt(docRef.get(Source.valueOf("favorites")).toString()) + 1);
        docRef.update(updates);
    }
}
