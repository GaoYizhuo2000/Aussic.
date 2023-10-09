package au.edu.anu.Aussic.models.firebase;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import au.edu.anu.Aussic.models.entity.User;

public class FirestoreDaoImpl implements FirestoreDao {
    FirebaseFirestore firestore = SingletonFirestoreDbConnection.getInstance();
    CollectionReference songsRef = firestore.collection("Songs");
    CollectionReference usersRef = firestore.collection("users");
    List<String> idList = null;

    @Override
    public void updateSongs() {


    }

    @Override
    public CompletableFuture<Map<String, Object>> getRandomSong() {
        CompletableFuture<Map<String, Object>> resultFuture = new CompletableFuture<>();
        if(idList == null){
            DocumentReference docRef = firestore.collection("idList").document("idList");
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Map<String, Object> data = task.getResult().getData();
                    List<String> idList = (List<String>) data.get("idList");
                    //随机取一个id search
                    Random random = new Random();
                    int index = random.nextInt(idList.size());
                    String id = idList.get(index);
                    Map<String, Object> terms= new HashMap<>();
                    terms.put("id", id);
                    terms.put("artistName", null);
                    terms.put("name", null);
                    terms.put("releaseDate", null);
                    searchSongs(terms).thenAccept(songResult -> {
                        resultFuture.complete(songResult.get(0));
                    });
                }
            });
        }else{
            Random random = new Random();
            int index = random.nextInt(idList.size());
            String id = idList.get(index);
            Map<String, Object> terms= new HashMap<>();
            terms.put("id", id);
            terms.put("artistName", null);
            terms.put("name", null);
            terms.put("releaseDate", null);
            searchSongs(terms).thenAccept(songResult -> {
                resultFuture.complete(songResult.get(0));
            });
        }
        return resultFuture;
    }

    @Override
    public CompletableFuture<List<Map<String, Object>>> searchSongs(Map<String, Object> terms) {
        Query query = songsRef;
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        List<Map<String, Object>> results = new ArrayList<>();
        String artistName = (String) terms.get("artistName");
        String name = (String) terms.get("name");
        String releaseDate = (String) terms.get("releaseDate");
        String id = (String) terms.get("id");

        if(artistName != null){
            query = query.whereEqualTo(FieldPath.of("attributes","artistName"), artistName);
        }
        if(name != null){
            query = query.whereEqualTo(FieldPath.of("attributes","name"), name);
        }
        if(releaseDate != null){
            query = query.whereEqualTo(FieldPath.of("attributes","releaseDate"), releaseDate);
        }
        if(id != null){
            query = query.whereEqualTo(FieldPath.of("id"), id);
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                for(QueryDocumentSnapshot document: documents){
                    results.add(document.getData());
                }
                future.complete(results);
            }
        });

        return future;
    }

    @Override
    public void addUserdata(User user) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> userdata = gson.fromJson(gson.toJson(user), type);
        usersRef.document((String) userdata.get("username")).set(userdata);
    }


}
