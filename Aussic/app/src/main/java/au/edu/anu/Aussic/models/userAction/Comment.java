package au.edu.anu.Aussic.models.userAction;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.models.firestoreSingleton.Firestore;

/**
 *
 *
 * @author Yizhuo Gao
 */
public class Comment extends UserAction{
    protected String content;

    public Comment(String actionType, String username, String targetSong, Integer targetSongId, String content) {
        super(actionType, username, targetSong, targetSongId);
        this.content = content;
    }

    @Override
    public String getToastMessage() {
        return String.format("User %s commented on the song \"%s\": %s", username, targetSong, content);
    }

    @Override
    public void update() {
        FirebaseFirestore db = Firestore.getInstance();
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
