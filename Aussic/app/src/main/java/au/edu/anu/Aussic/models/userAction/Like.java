package au.edu.anu.Aussic.models.userAction;

/**
 * @author Yizhuo Gao
 */
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import au.edu.anu.Aussic.models.firebase.SingletonFirestoreDbConnection;

public class Like extends UserAction{
    public Like(String actionType, String username, String targetSong, Integer targetSongId) {
        super(actionType, username, targetSong, targetSongId);
    }



    @Override
    public String getToastMessage() {
        return String.format("User %s liked the song \"%s\"", username, targetSong);
    }
    /**
     * update attributes in songs according to this useraction
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
                Long likes = (Long) data.get("likes") + 1;
                docRef.update("likes", likes);
            }
        });
        //之后在listview中添加监听器，同步显示数据变化
    }
}
