package au.edu.anu.Aussic.models.userAction;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

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
        DocumentReference docRef = db.collection("songs").document(targetSongId + "");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                Map<String, Object> comments = (Map<String, Object>) data.get("comments");
                comments.put("num", Integer.parseInt((String) comments.get("num")) + 1);
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
