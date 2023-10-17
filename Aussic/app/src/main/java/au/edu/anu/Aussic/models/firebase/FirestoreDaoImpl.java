package au.edu.anu.Aussic.models.firebase;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.GsonLoader.GsonLoader;
import au.edu.anu.Aussic.models.entity.Session;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;

public class FirestoreDaoImpl implements FirestoreDao {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    public FirebaseFirestore firestore = SingletonFirestoreDbConnection.getInstance();
    public CollectionReference songsRef = firestore.collection("Songs");
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

                    User newUsr = GsonLoader.loadUser(usrData);


                    usr.setUsr(newUsr);
                    // Notify subsequent change
                    RuntimeObserver.notifyOnDataChangeListeners();
                }
            }
        });
    }

    @Override
    public void setSessionCollectionRealtimeListener(){
        sessionsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Session session = GsonLoader.loadSession(dc.getDocument().getData());
                            if(session.getUsers().get(0).equals(currentUser.getEmail()) || session.getUsers().get(1).equals(currentUser.getEmail())){
                                FirestoreDao firestoreDao = new FirestoreDaoImpl();
                                firestoreDao.setSessionRealTimeListener(session);
                                if(!RuntimeObserver.currentUserSessions.contains(session)) RuntimeObserver.currentUserSessions.add(session);
                                List<String> list = new ArrayList<>();
                                list.add(session.getUsers().get(0).equals(currentUser.getEmail()) ? session.getUsers().get(1) : session.getUsers().get(0));
                                firestoreDao.getUsersData(list).thenAccept(results->{
                                    List<Map<String, Object>> maps = new ArrayList<>();
                                    maps.addAll(results);
                                    for(Map<String, Object> map : maps) {
                                        User newUser = GsonLoader.loadUser(map);
                                        firestoreDao.setUsrRealTimeListener(newUser);
                                        if(!RuntimeObserver.currentSessionsAvailableUsers.contains(newUser)) RuntimeObserver.currentSessionsAvailableUsers.add(newUser);
                                    }
                                });

                                RuntimeObserver.notifyOnDataChangeListeners();
                            }
                            break;
                    }
                }
            }
        });

    }

    @Override
    public void setSessionRealTimeListener(Session session){
        DocumentReference sessionRef = sessionsRef.document(session.getName());
        sessionRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (snapshot != null && snapshot.exists()) {
                    Map<String, Object> sessionData = snapshot.getData();

                    Session newSession = GsonLoader.loadSession(sessionData);

                    session.setSession(newSession);

                    // Notify subsequent change
                    RuntimeObserver.notifyOnDataChangeListeners();
                }
            }
        });

    }

    @Override
    public void updateBlockList(String id){ usersRef.document(currentUser.getEmail()).update("blockList", FieldValue.arrayUnion(id)); }

    @Override
    public void removeBlockList(String id){
        DocumentReference docRef = usersRef.document(currentUser.getEmail());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Map<String, Object> userdata = task.getResult().getData();
                List<String> blockList = new ArrayList<>();
                if(userdata.get("blockList") != null){
                    blockList = (List<String>)userdata.get("blockList");
                }
                blockList.remove(id);
                docRef.update("blockList", blockList);
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
        List<Task> allTask = new ArrayList<>();
        int elementCount = 0;
        Query query = songsRef;
        Task<DocumentSnapshot> taskUsrSearch;
        Task<DocumentSnapshot> taskArtistSearch;
        Task<DocumentSnapshot> taskGenreSearch;
        Task<QuerySnapshot> taskSongSearch;
        CompletableFuture<List<Map<String, Object>>> future = new CompletableFuture<>();
        List<Map<String, Object>> results = new ArrayList<>();
        AtomicInteger flag = new AtomicInteger();

        if(terms.containsKey("undefinedTerm")&& terms.get("undefinedTerm") != null){// general search

            Task<QuerySnapshot> task1 = query.whereEqualTo(FieldPath.of("attributes", "name"), terms.get("undefinedTerm")).get();
            Task<DocumentSnapshot> task2 = artistsRef.document(terms.get("undefinedTerm")).get();
            Task<DocumentSnapshot> task3 = genresRef.document(terms.get("undefinedTerm")).get();
            Task<DocumentSnapshot> task4 = usersRef.document(terms.get("undefinedTerm")).get();

            // 使用Tasks.whenAllSuccess来等待所有任务完成
            Tasks.whenAllSuccess(task1, task2, task3, task4).addOnCompleteListener(task -> {
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
                    if (task4.isSuccessful()) {
                        results.add(task4.getResult().getData());
                    }
                    future.complete(results);
                }
            });

        }else{
            if(terms.containsKey("artistName")&& terms.get("artistName") != null){
                query = query.whereEqualTo(FieldPath.of("attributes","artistName"), terms.get("artistName"));
                taskArtistSearch = artistsRef.document(terms.get("artistName")).get();
                allTask.add(taskArtistSearch);
                elementCount++;
            }
            if(terms.containsKey("genre")&& terms.get("genre") != null){
                query = query.whereArrayContains(FieldPath.of("attributes","genreNames"), terms.get("genre"));
                taskGenreSearch = genresRef.document(terms.get("genre")).get();
                allTask.add(taskGenreSearch);
                elementCount++;
            }
            if(terms.containsKey("user")&& terms.get("user") != null) {
                taskUsrSearch = usersRef.document(terms.get("user")).get();
                allTask.add(taskUsrSearch);
            }
            if(terms.containsKey("name")&& terms.get("name") != null){
                query = query.whereEqualTo(FieldPath.of("attributes","name"), terms.get("name"));
                elementCount++;
            }
            if(terms.containsKey("releaseDate")&& terms.get("releaseDate") != null){
                query = query.whereEqualTo(FieldPath.of("attributes","releaseDate"), terms.get("releaseDate"));
                elementCount++;
            }
            if(terms.containsKey("id")&& terms.get("id") != null){
                query = query.whereEqualTo(FieldPath.of("id"), terms.get("id"));
                elementCount++;
            }

            if (elementCount > 0) {
                taskSongSearch = query.get();
                allTask.add(taskSongSearch);
            }

            int finalElementCount = elementCount;
            if (allTask.isEmpty()) return future;
            Tasks.whenAllSuccess(allTask).addOnCompleteListener(task -> {
                if(finalElementCount == 0){
                    if (allTask.get(0).isSuccessful()) {
                        results.add(((DocumentSnapshot)(allTask.get(0).getResult())).getData());
                    }
                }
                else {
                    if (allTask.get(allTask.size() - 1).isSuccessful()) {
                        QuerySnapshot documents = (QuerySnapshot) allTask.get(allTask.size() - 1).getResult();
                        for(QueryDocumentSnapshot document: documents){
                            results.add(document.getData());
                        }
                    }
                    for(int i = 0; i < allTask.size() - 1; i++){
                        if (allTask.get(i).isSuccessful()) {
                            results.add(((DocumentSnapshot)(allTask.get(i).getResult())).getData());
                        }
                    }
                }
                future.complete(results);
            });
        }

        return future;
    }

    @Override
    public void addUserdata(User user) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> userdata = gson.fromJson(gson.toJson(user), type);
        userdata.put("type", "users");
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
    public CompletableFuture <List<Map<String, Object>>> getUsersData(List<String> IDs){
        List<Map<String, Object>> results = new ArrayList<>();
        CompletableFuture<List<Map<String, Object>>> future= new CompletableFuture<>();
        List<Task> allTask = new ArrayList<>();
        for (String id : IDs){
            DocumentReference userdataRef = firestore.collection("users").document(id);
            Task<DocumentSnapshot> oneTask = userdataRef.get();
            allTask.add(oneTask);
        }

        Tasks.whenAllSuccess(allTask).addOnCompleteListener(task -> {
            for(int i = 0; i < allTask.size(); i++){
                if (allTask.get(i).isSuccessful()) {
                    results.add(((DocumentSnapshot)(allTask.get(i).getResult())).getData());
                }
            }
            future.complete(results);
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
    public CompletableFuture<Map<String, Object>> getSession(String sessionName){
        DocumentReference sessionDataRef = sessionsRef.document(sessionName);
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();

        sessionDataRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(task.getResult().getData());
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
        sessionData.put("name", currentUser.getEmail() + "&" + targetUserName);
        sessionsRef.document(currentUser.getEmail() + "&" + targetUserName).set(sessionData);
    }

    @Override
    public void updateHistory(String sessionId, String message) {
        Map<String, Object> newMessage = new HashMap<>();
        newMessage.put("message", message);
        newMessage.put("userName", currentUser.getEmail());
//        newMessage.put(currentUser.getEmail(),message);
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
