package au.edu.anu.Aussic.models.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    public CompletableFuture<List<Map<String, Object>>> getRandomSongs(int number) {
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();

        DocumentReference docRef = firestore.collection("idList").document("idList");
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> data = task.getResult().getData();
                List<String> idList = (List<String>) data.get("idList");
                Random random = new Random();

                // Sample unique random indices
                Set<Integer> indices = new HashSet<>();
                while (indices.size() < Math.min(number, idList.size())) {
                    indices.add(random.nextInt(idList.size()));
                }

                List<String> randomIds = indices.stream().map(idList::get).collect(Collectors.toList());

                // Fetch the first song (we'll do this one-by-one to avoid complications)
                fetchSongById(randomIds, 0, new ArrayList<>(), future);

            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<Map<String, Object>> getRandomUseraction() {
        Random random = new Random();
        DocumentReference useractionRef = firestore.collection("useractions").document(Integer.toString(random.nextInt(2500)));
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();
        useractionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(task.getResult().getData());
            }
        });
        return future;
    }

    private void fetchSongById(List<String> ids, int index, List<Map<String, Object>> accumulated, CompletableFuture<List<Map<String, Object>>> future) {
        if (index >= ids.size()) {
            future.complete(accumulated);
            return;
        }

        songsRef.document(ids.get(index)).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                accumulated.add(task.getResult().getData());
                fetchSongById(ids, index + 1, accumulated, future);
            } else {
                future.completeExceptionally(task.getException());
            }
        });
    }


    //search songs according to terms, to be improved later
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

    @Override
    public CompletableFuture<Map<String, Object>> getUserdata(FirebaseUser user) {
        DocumentReference userdataRef = firestore.collection("users").document(user.getEmail());
        CompletableFuture<Map<String, Object>> future= new CompletableFuture<>();
        userdataRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(task.getResult().getData());
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<String> updateUserFavorites(String songId) {
        CompletableFuture<String> future = new CompletableFuture<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = user.getEmail();
        DocumentReference docRef = firestore.collection("users").document(username);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> userdata = task.getResult().getData();
                List<String> favorites = new ArrayList<>();
                String msg = null;
                if(userdata.get("favorites") != null){
                    favorites = (List<String>)userdata.get("favorites");
                }
                if(!favorites.contains(songId)){
                    favorites.add(songId);
                    docRef.update("favorites", favorites);
                }else {
                    msg = "Already added to favorites";
                }
                future.complete(msg);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<String> updateUserLikes(String songId) {
        CompletableFuture<String> future = new CompletableFuture<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = user.getEmail();
        DocumentReference docRef = firestore.collection("users").document(username);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> userdata = task.getResult().getData();
                List<String> likes = new ArrayList<>();
                String msg = null;
                if(userdata.get("likes") != null){
                    likes = (List<String>)userdata.get("likes");
                }
                if(!likes.contains(songId)){
                    likes.add(songId);
                    docRef.update("likes", likes);
                }else {
                    msg = "Already liked this song";
                }
                future.complete(msg);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<List<Map<String, Object>>> getComment(String songId) {
        DocumentReference docRef = firestore.collection("Songs").document(songId);
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> data = task.getResult().getData();
                Map<String, Object> comments = (Map<String, Object>) data.get("comments");
                List<Map<String, Object>> details = (List<Map<String, Object>>) comments.get("details");
                future.complete(details);
            }
        });
        return future;
    }


}
