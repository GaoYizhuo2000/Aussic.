package au.edu.anu.Aussic.models.firebase;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import au.edu.anu.Aussic.models.entity.User;

public interface FirestoreDao {
    void updateSongs();

    CompletableFuture<Map<String, Object>> getRandomSong();
    CompletableFuture<List<Map<String, Object>>> searchSongs(Map<String, Object> terms);
    void addUserdata(User user);
}
