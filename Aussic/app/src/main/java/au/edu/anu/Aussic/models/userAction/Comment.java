package au.edu.anu.Aussic.models.userAction;

import com.google.firebase.firestore.FirebaseFirestore;

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
        System.out.println(db + "");
    }
}
