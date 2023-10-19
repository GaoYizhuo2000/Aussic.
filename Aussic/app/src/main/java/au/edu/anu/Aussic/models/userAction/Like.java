package au.edu.anu.Aussic.models.userAction;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import au.edu.anu.Aussic.models.firebase.SingletonFirestoreDbConnection;

/**
 * Represents a user action of liking a song in the application.
 * Extends the UserAction class to capture user actions related to song liking.
 *
 * @author u7552399, Yizhuo Gao
 */

public class Like extends UserAction{
    /**
     * Constructs a Like user action with the provided parameters.
     *
     * @param actionType    The type of user action (e.g., "like").
     * @param username      The username of the user who performed the action.
     * @param targetSong    The name of the song that was liked.
     * @param targetSongId  The unique identifier (ID) of the liked song.
     */
    public Like(String actionType, String username, String targetSong, Integer targetSongId) {
        super(actionType, username, targetSong, targetSongId);
    }



    /**
     * Gets a toast message representing the user's liking action.
     *
     * @return A formatted toast message indicating that the user liked a song.
     */
    @Override
    public String getToastMessage() {
        return String.format("User %s liked the song \"%s\"", username, targetSong);
    }


    /**
     * Updates attributes in the songs collection according to this user action.
     * Increments the number of likes for the target song.
     */
    @Override
    public void update() {
        FirebaseFirestore db = SingletonFirestoreDbConnection.getInstance();
        DocumentReference docRef = db.collection("Songs").document(targetSongId + "");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> data = task.getResult().getData();
                Long likes = (Long) data.get("likes") + 1;
                docRef.update("likes", likes);
            }
        });
    }
}
