package au.edu.anu.Aussic.models.firebase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FirestoreDaoImpl implements FirestoreDao {
    FirebaseFirestore firestore = SingletonFirestoreDbConnection.getInstance();
    CollectionReference songsRef = firestore.collection("Songs");

    @Override
    public void updateSongs() {

    }

    @Override
    public CompletableFuture<List<Map<String, Object>>> searchSongs(Map<String, Object> terms) {
        Query query = songsRef;
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        List<Map<String, Object>> results = new ArrayList<>();
        String artistName = (String) terms.get("artistName");
        String name = (String) terms.get("name");
        String releaseDate = (String) terms.get("releaseDate");

        if(artistName != null){
            query = query.whereEqualTo(FieldPath.of("attributes","artistName"), artistName);
        }
        if(name != null){
            query = query.whereEqualTo(FieldPath.of("attributes","name"), name);
        }
        if(releaseDate != null){
            query = query.whereEqualTo(FieldPath.of("attributes","releaseDate"), releaseDate);
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                for(QueryDocumentSnapshot document: documents){
                    results.add(document.getData());
                }
                future.complete(results);
            }else {
                 // 如果出现异常，完成 Future 并传递异常
            }
        });
        return future;
    }


}
