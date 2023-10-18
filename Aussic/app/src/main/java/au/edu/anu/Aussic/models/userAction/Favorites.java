package au.edu.anu.Aussic.models.userAction;

/**
 * @author Yizhuo Gao
 */

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import au.edu.anu.Aussic.models.firebase.SingletonFirestoreDbConnection;

public class Favorites extends UserAction{


    public Favorites(String actionType, String username, String targetSong, Integer targetSongId) {
        super(actionType, username, targetSong, targetSongId);
    }

    @Override
    public String getToastMessage() {
        return String.format("User %s added the song \"%s\" to favorites", username, targetSong);
    }
    /**
     * update attributes in songs collection according to this useraction
     *
     *
     * */
    @Override
    public void update() {

        FirebaseFirestore db = SingletonFirestoreDbConnection.getInstance();
        DocumentReference docRef = db.collection("Songs").document(targetSongId + "");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> data = task.getResult().getData();
                Long favorites = (Long) data.get("favorites") + 1;
                docRef.update("favorites", favorites);
            }
        });

    }
}
