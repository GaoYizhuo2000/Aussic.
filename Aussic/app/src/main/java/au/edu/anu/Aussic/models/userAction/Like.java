package au.edu.anu.Aussic.models.userAction;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import au.edu.anu.Aussic.models.firestoreSingleton.Firestore;

/**
 *
 *
 * @author Yizhuo Gao
 */
public class Like extends UserAction{
    public Like(String actionType, String username, String targetSong, Integer targetSongId) {
        super(actionType, username, targetSong, targetSongId);
    }



    @Override
    public String getToastMessage() {
        return String.format("User %s liked the song \"%s\"", username, targetSong);
    }

    @Override
    public void update() {
        FirebaseFirestore db = Firestore.getInstance();
        DocumentReference docRef = db.collection("songs").document(targetSongId + "");
        Map<String, Object> updates = new HashMap<>();
        updates.put("likes", Integer.parseInt(docRef.get(Source.valueOf("likes")).toString()) + 1);
        docRef.update(updates);
        //之后在listview中添加监听器，同步显示数据变化
    }
}
