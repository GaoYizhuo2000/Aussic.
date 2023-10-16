package au.edu.anu.Aussic.models.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.SongLoader.GsonLoader;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;

public class FirestoreDaoImpl implements FirestoreDao {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firestore = SingletonFirestoreDbConnection.getInstance();
    CollectionReference songsRef = firestore.collection("Songs");
    CollectionReference usersRef = firestore.collection("users");
    CollectionReference artistsRef = firestore.collection("artists");
    CollectionReference genresRef = firestore.collection("genres");
    CollectionReference sessionsRef = firestore.collection("sessions");
    List<String> idList = null;

    public void setSongRealTimeListener(Song song){

        DocumentReference songRef = songsRef.document(song.getId());
        songRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    song.setSong(GsonLoader.loadSong(snapshot.getData()));
                    // Notify subsequent change
                    RuntimeObserver.notifyOnDataChangeListeners();
                }
            }
        });
    }

    public void setUsrRealTimeListener(User usr){

        DocumentReference usrRef = usersRef.document(usr.username);
        usrRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    Map<String, Object> usrData = snapshot.getData();
                    String iconUrl = (String) usrData.get("iconUrl");
                    String usrName = (String) usrData.get("username");

                    User newUsr = new User(usrName, iconUrl);

                    for(String songID : (List<String>)usrData.get("favorites")) newUsr.addFavorites(songID);
                    for(String songID : (List<String>)usrData.get("likes")) newUsr.addLikes(songID);

                    usr.setUsr(newUsr);
                    // Notify subsequent change
                    RuntimeObserver.notifyOnDataChangeListeners();
                }
            }
        });
    }

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
                    Map<String, String> terms= new HashMap<>();
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
            Map<String, String> terms= new HashMap<>();
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
    public CompletableFuture<List<Map<String, Object>>> searchSongs(Map<String, String> terms) {
        Query query = songsRef;
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        List<Map<String, Object>> results = new ArrayList<>();
        AtomicInteger flag = new AtomicInteger();

        if(terms.containsKey("undefinedTerm")&& terms.get("undefinedTerm") != null){// general search

            Task<QuerySnapshot> task1 = query.whereEqualTo(FieldPath.of("attributes", "name"), terms.get("undefinedTerm")).get();
            Task<DocumentSnapshot> task2 = artistsRef.document(terms.get("undefinedTerm")).get();
            Task<DocumentSnapshot> task3 = genresRef.document(terms.get("undefinedTerm")).get();

            // 使用Tasks.whenAllSuccess来等待所有任务完成
            Tasks.whenAllSuccess(task1, task2, task3).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task1.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task1.getResult()) {
                            results.add(document.getData());
                        }
                    }

                    if (task2.isSuccessful()) {
                        results.add(task2.getResult().getData());
                    }

                    if (task3.isSuccessful()) {
                        results.add(task3.getResult().getData());
                    }
                    future.complete(results);
                }
            });

        }else{
            if(terms.containsKey("artistName")&& terms.get("artistName") != null){
                query = query.whereEqualTo(FieldPath.of("attributes","artistName"), terms.get("artistName"));
            }
            if(terms.containsKey("name")&& terms.get("name") != null){
                query = query.whereEqualTo(FieldPath.of("attributes","name"), terms.get("name"));
            }
            if(terms.containsKey("releaseDate")&& terms.get("releaseDate") != null){
                query = query.whereEqualTo(FieldPath.of("attributes","releaseDate"), terms.get("releaseDate"));
            }
            if(terms.containsKey("id")&& terms.get("id") != null){
                query = query.whereEqualTo(FieldPath.of("id"), terms.get("id"));
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
        }

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
        String username = currentUser.getEmail();
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
    public void deleteUserFavorites(String songId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String username = user.getEmail();
        DocumentReference docRef = firestore.collection("users").document(username);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> userdata = task.getResult().getData();
                List<String> favorites = new ArrayList<>();
                if(userdata.get("favorites") != null){
                    favorites = (List<String>)userdata.get("favorites");
                }
                favorites.remove(songId);
                docRef.update("favorites", favorites);
            }
        });
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
    public CompletableFuture<List<Map<String, Object>>> getSongsByIdList(List<String> songIdList) {
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        List<Map<String, Object>> results = new ArrayList<>();
        Query query = songsRef.whereIn(FieldPath.documentId(), songIdList);
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
    public CompletableFuture<List<Map<String, Object>>> getAllUsers() {
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        List<Map<String, Object>> users = new ArrayList<>();
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot documents = task.getResult();
                for(DocumentSnapshot document: documents){
                    users.add(document.getData());
                }
                future.complete(users);
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<List<Map<String, Object>>> getSessions() {
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        List<Map<String, Object>> sessionList = new ArrayList<>();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Query query = sessionsRef.whereArrayContains("users", userName);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    sessionList.add(document.getData());
                }
                future.complete(sessionList);
            }
        });
        return future;
    }

    @Override
    public void createSession(String targetUserName) {
        Map<String, Object> sessionData = new HashMap<>();
        List<String> users = new ArrayList<>();
        users.add(currentUser.getEmail());
        users.add(targetUserName);
        List<Map<String, Object>> history = new ArrayList<>();
        sessionData.put("users", users);
        sessionData.put("history", history);
        sessionsRef.document(currentUser.getEmail() + "&" + targetUserName).set(sessionData);
    }

    @Override
    public void updateHistory(String sessionId, String message) {
        Map<String, Object> newMessage = new HashMap<>();
        newMessage.put(currentUser.getEmail(),message);
        sessionsRef.document(sessionId).update("history", FieldValue.arrayUnion(newMessage));
    }

    @Override
    public CompletableFuture<List<Map<String, Object>>> loadRandomGenres(int num){
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();

        DocumentReference docRef = firestore.collection("genreList").document("genreList");
        docRef.get().addOnCompleteListener(task -> {
            Map<String, Object> data = task.getResult().getData();
            List<String> genreList = (List<String>) data.get("genreList");
            loadGenresHelper(future, genreList, num);
        });

        return future;
    }

    private CompletableFuture<List<Map<String, Object>>> loadGenresHelper(CompletableFuture<List<Map<String, Object>>> future, List<String> data, int num){
        Set<String> randStrings= new HashSet<>();
        Random random = new Random();

        while(randStrings.size() < num) {
            randStrings.add(data.get(random.nextInt(data.size())));
        }
        List<Task<QuerySnapshot>> tasks = new ArrayList<>();

        for (String randString : randStrings) {
            Task<QuerySnapshot> newTask = genresRef
                    .orderBy("genreName")
                    .startAt(randString)
                    .limit(1)
                    .get();
            tasks.add(newTask);
        }

        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> results = new ArrayList<>();
                        for (Task<QuerySnapshot> individualTask : tasks) {
                            DocumentSnapshot snapshot = individualTask.getResult().getDocuments().get(0);

                            Map<String, Object> genreData = snapshot.getData();
                            results.add(genreData);
                        }
                        future.complete(results);
                    }
                });

        return future;
    }

}
