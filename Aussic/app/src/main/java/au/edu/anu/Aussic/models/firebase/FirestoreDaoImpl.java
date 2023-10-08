package au.edu.anu.Aussic.models.firebase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirestoreDaoImpl implements FirestoreDao {
    FirebaseFirestore firestore = SingletonFirestoreDbConnection.getInstance();
    CollectionReference songsRef = firestore.collection("Songs");

    @Override
    public void updateSongs() {

    }

    @Override
    public List<Map<String, Object>> searchSongs(Map<String, Object> terms) {
        Query query = songsRef;
        List<Map<String, Object>> results = new ArrayList<>();
        String artistName = (String) terms.get("artistName");
        String name = (String) terms.get("name");
        String releaseDate = (String) terms.get("releaseDate");

        if(artistName != null){
            query = query.whereEqualTo("artistName", artistName);
        }
        if(name != null){
            query = query.whereEqualTo("name", name);
        }
        if(releaseDate != null){
            query = query.whereEqualTo("releaseDate", releaseDate);
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                for(QueryDocumentSnapshot document: documents){
                    results.add(document.getData());
                }
            }
        });
        return results;
    }


}
