package au.edu.anu.Aussic.models.userAction;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import au.edu.anu.Aussic.models.firebase.SingletonFirestoreDbConnection;

/**
 * The `Favorites` class represents a user action of adding a song to favorites.
 * It extends the `UserAction` class and provides functionality for updating song attributes based on the favorites action.
 *
 * Example usage:
 * ```java
 * Favorites favorites = new Favorites("Favorites", "john_doe", "Song Title", 123);
 * favorites.update();
 * ```
 *
 * The `Favorites` class allows users to add songs to their favorites, and it updates the number of times a song has been favorited in the Firebase Firestore database.
 *
 * @author u7552399, Yizhuo Gao
 */

public class Favorites extends UserAction{

    /**
     * Constructs a `Favorites` object with the specified parameters.
     *
     * @param actionType    The type of user action (e.g., "Favorites").
     * @param username      The username of the user who added the song to favorites.
     * @param targetSong    The title of the song that is added to favorites.
     * @param targetSongId  The ID of the target song.
     */
    public Favorites(String actionType, String username, String targetSong, Integer targetSongId) {
        super(actionType, username, targetSong, targetSongId);
    }

    /**
     * Gets a toast message describing the favorites action.
     *
     * @return A toast message.
     */
    @Override
    public String getToastMessage() {
        return String.format("User %s added the song \"%s\" to favorites", username, targetSong);
    }


    /**
     * Updates song attributes in the Firebase Firestore database based on the favorites action.
     * It increments the number of times the song has been favorited.
     */
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
