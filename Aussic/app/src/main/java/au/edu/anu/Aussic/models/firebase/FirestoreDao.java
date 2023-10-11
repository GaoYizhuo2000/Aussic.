package au.edu.anu.Aussic.models.firebase;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import au.edu.anu.Aussic.models.entity.User;

public interface FirestoreDao {
    void updateSongs();

    CompletableFuture<Map<String, Object>> getRandomSong();
    CompletableFuture<List<Map<String, Object>>> getRandomSongs(int number);
    CompletableFuture<Map<String, Object>> getRandomUseraction();
    CompletableFuture<List<Map<String, Object>>> searchSongs(Map<String, Object> terms);
    void addUserdata(User user);
    CompletableFuture<Map<String, Object>> getUserdata(FirebaseUser user);
    CompletableFuture<String> updateUserFavorites(String songId);
    CompletableFuture<String> updateUserLikes(String songId);
    CompletableFuture<Map<String, Object>> getComment(String songId);

}
