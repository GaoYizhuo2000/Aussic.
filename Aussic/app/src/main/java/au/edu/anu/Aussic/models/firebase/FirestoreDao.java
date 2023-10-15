package au.edu.anu.Aussic.models.firebase;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;

public interface FirestoreDao {
    void updateSongs();

    CompletableFuture<Map<String, Object>> getRandomSong();
    CompletableFuture<List<Map<String, Object>>> getRandomSongs(int number);
    CompletableFuture<Map<String, Object>> getRandomUseraction();
    CompletableFuture<List<Map<String, Object>>> searchSongs(Map<String, String> terms);
    void addUserdata(User user);
    CompletableFuture<Map<String, Object>> getUserdata(FirebaseUser user);
    CompletableFuture<String> updateUserFavorites(String songId);
    void deleteUserFavorites(String songId);
    CompletableFuture<String> updateUserLikes(String songId);
    CompletableFuture<List<Map<String, Object>>> getComment(String songId);
    CompletableFuture<List<Map<String, Object>>> getSongsByIdList(List<String> songIdList);


    void setSongRealTimeListener(Song song);
    void setUsrRealTimeListener(User usr);

}
