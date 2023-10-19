package au.edu.anu.Aussic.models.userAction;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.models.firebase.SingletonFirestoreDbConnection;

/**
 * The `Comment` class represents a user action of adding a comment to a song.
 * It extends the `UserAction` class and provides functionality for updating song attributes based on the comment.
 *
 * Example usage:
 * ```java
 * Comment comment = new Comment("Comment", "john_doe", "Song Title", 123, "Great song!");
 * comment.update();
 * ```
 *
 * The `Comment` class allows users to add comments to songs, and the comments are stored in the Firebase Firestore database.
 * It updates the comments section of the target song, including the number of comments and the comment details.
 *
 * @author u7552399, Yizhuo Gao
 */
public class Comment extends UserAction{
    protected String content;

    /**
     * Constructs a `Comment` object with the specified parameters.
     *
     * @param actionType    The type of user action (e.g., "Comment").
     * @param username      The username of the user who made the comment.
     * @param targetSong    The title of the song that the comment is added to.
     * @param targetSongId  The ID of the target song.
     * @param content       The content of the comment.
     */
    public Comment(String actionType, String username, String targetSong, Integer targetSongId, String content) {
        super(actionType, username, targetSong, targetSongId);
        this.content = content;
    }

    /**
     * Gets a toast message describing the comment action.
     *
     * @return A toast message.
     */
    @Override
    public String getToastMessage() {
        return String.format("User %s commented on the song \"%s\": %s", username, targetSong, content);
    }

    /**
     * Updates song attributes in the Firebase Firestore database based on the comment action.
     * It increments the number of comments and adds the comment details to the target song.
     */
    @Override
    public void update() {
        FirebaseFirestore db = SingletonFirestoreDbConnection.getInstance();
        DocumentReference docRef = db.collection("Songs").document("" + targetSongId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> data = task.getResult().getData();
                Map<String, Object> comments = (Map<String, Object>) data.get("comments");
                Long num = (Long) comments.get("num");
                comments.put("num", num + 1);
                List<Map<String, Object>> details = (List<Map<String, Object>>) comments.get("details");
                Map<String, Object> newComment = new HashMap<>();
                newComment.put("uid", username);
                newComment.put("content", content);
                details.add(newComment);
                comments.put("details", details);
                docRef.update("comments", comments);
            }
        });
    }
}
